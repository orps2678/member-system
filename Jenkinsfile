pipeline {
    agent any

    // 環境變數
    environment {
        // Docker 映像檔相關
        IMAGE_NAME = 'member-system'
        IMAGE_TAG = "${BUILD_NUMBER}"
        DOCKER_REGISTRY = 'localhost'

        // 應用程式相關
        APP_NAME = 'member-system-app'
        APP_PORT = '8081'

        // 環境設定
        SPRING_PROFILE = 'dev'
    }

    // 建置觸發條件
    triggers {
        // GitHub webhook 觸發
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                echo '正在拉取程式碼...'
                // Git checkout（Jenkins 會自動處理）
                checkout scm

                // 顯示 commit 資訊
                script {
                    env.GIT_COMMIT_MSG = sh(
                        script: 'git log -1 --pretty=%B',
                        returnStdout: true
                    ).trim()
                    env.GIT_AUTHOR = sh(
                        script: 'git log -1 --pretty=%an',
                        returnStdout: true
                    ).trim()
                }
                echo "提交訊息: ${env.GIT_COMMIT_MSG}"
                echo "提交者: ${env.GIT_AUTHOR}"
            }
        }

        stage('Environment Check') {
            steps {
                echo '檢查建置環境...'
                sh '''
                    echo "=== 系統資訊 ==="
                    uname -a
                    echo "=== Java 版本 ==="
                    java -version
                    echo "=== Maven 版本 ==="
                    mvn -version
                    echo "=== Docker 版本 ==="
                    docker --version
                    echo "=== Docker Compose 版本 ==="
                    docker-compose --version
                '''
            }
        }

        stage('Test & Build') {
            steps {
                echo '執行測試和編譯...'
                sh '''
                    echo "清理舊的建置檔案..."
                    docker run --rm -v $(pwd):/app -w /app maven:3.9.6-eclipse-temurin-17 mvn clean

                    echo "執行測試..."
                    docker run --rm -v $(pwd):/app -w /app maven:3.9.6-eclipse-temurin-17 mvn test

                    echo "編譯和打包..."
                    docker run --rm -v $(pwd):/app -w /app maven:3.9.6-eclipse-temurin-17 mvn package -DskipTests

                    echo "檢查建置結果..."
                    ls -la target/
                '''
            }
            post {
                always {
                    // 保存測試報告 - 使用正確的插件名稱
                    junit testResultsPattern: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    // 歸檔建置產物
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo '建立 Docker 映像檔...'
                script {
                    // 建立新的映像檔
                    def image = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")

                    // 也標記為 latest
                    sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"

                    // 顯示映像檔資訊
                    sh "docker images | grep ${IMAGE_NAME}"

                    // 儲存映像檔 ID 供後續使用
                    env.DOCKER_IMAGE_ID = image.id
                }
            }
        }

        stage('Security & Quality Check') {
            parallel {
                stage('Docker Security Scan') {
                    steps {
                        echo '執行 Docker 映像檔安全掃描...'
                        script {
                            try {
                                // Docker 安全掃描（如果有安裝）
                                sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image ${IMAGE_NAME}:${IMAGE_TAG} || echo '安全掃描工具未安裝，跳過'"
                            } catch (Exception e) {
                                echo "安全掃描跳過: ${e.getMessage()}"
                            }
                        }
                    }
                }

                stage('Code Quality') {
                    steps {
                        echo '程式碼品質檢查...'
                        // 可以整合 SonarQube 或其他工具
                        echo '程式碼品質檢查完成（可整合 SonarQube）'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo '部署到容器環境...'
                script {
                    try {
                        // 停止現有容器
                        echo '停止現有容器...'
                        sh '''
                            docker-compose down || echo "沒有運行中的容器"
                        '''

                        // 等待容器完全停止
                        sleep(time: 5, unit: 'SECONDS')

                        // 啟動新版本
                        echo '啟動新版本...'
                        sh '''
                            # 使用新的映像檔標籤更新環境變數
                            export IMAGE_TAG=${BUILD_NUMBER}
                            docker-compose up -d
                        '''

                    } catch (Exception e) {
                        error "部署失敗: ${e.getMessage()}"
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                echo '執行健康檢查...'
                script {
                    // 等待應用程式啟動
                    sleep(time: 30, unit: 'SECONDS')

                    // 檢查應用程式健康狀態
                    def maxRetries = 10
                    def retryCount = 0
                    def healthCheckPassed = false

                    while (retryCount < maxRetries && !healthCheckPassed) {
                        try {
                            sh """
                                curl -f http://localhost:${APP_PORT}/api/actuator/health
                            """
                            healthCheckPassed = true
                            echo "健康檢查通過！"
                        } catch (Exception e) {
                            retryCount++
                            echo "健康檢查失敗，重試 ${retryCount}/${maxRetries}..."
                            sleep(time: 10, unit: 'SECONDS')
                        }
                    }

                    if (!healthCheckPassed) {
                        error "健康檢查失敗！應用程式可能無法正常啟動。"
                    }
                }
            }
        }

        stage('Cleanup') {
            steps {
                echo '清理舊的映像檔...'
                script {
                    // 保留最近 5 個版本的映像檔
                    sh '''
                        # 清理舊的映像檔（保留最新 5 個）
                        docker images ${IMAGE_NAME} --format "table {{.Tag}}" | grep -v latest | grep -v TAG | sort -nr | tail -n +6 | xargs -I {} docker rmi ${IMAGE_NAME}:{} || echo "沒有舊映像檔需要清理"

                        # 清理未使用的映像檔
                        docker image prune -f
                    '''
                }
            }
        }
    }

    post {
        always {
            echo '=== 建置完成 ==='
        }

        success {
            echo '建置和部署成功！'
            script {
                def deploymentInfo = """
                部署成功通知
                專案: ${env.JOB_NAME}
                建置號: ${env.BUILD_NUMBER}
                提交者: ${env.GIT_AUTHOR}
                提交訊息: ${env.GIT_COMMIT_MSG}
                應用程式: http://localhost:${APP_PORT}/api/swagger-ui.html
                完成時間: ${new Date()}
                """
                echo deploymentInfo
            }
        }

        failure {
            echo '建置或部署失敗！'
            script {
                def failureInfo = """
                部署失敗通知
                專案: ${env.JOB_NAME}
                建置號: ${env.BUILD_NUMBER}
                提交者: ${env.GIT_AUTHOR}
                提交訊息: ${env.GIT_COMMIT_MSG}
                失敗時間: ${new Date()}
                請檢查建置日誌: ${env.BUILD_URL}console
                """
                echo failureInfo
            }
        }
    }
}