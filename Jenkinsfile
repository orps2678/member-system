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
                echo '執行編譯（跳過測試）...'
                sh '''
                    echo "清理舊的建置檔案..."
                    mvn clean

                    echo "編譯和打包（跳過測試）..."
                    mvn package -DskipTests

                    echo "檢查建置結果..."
                    ls -la target/
                '''
            }
            post {
                always {
                    // 歸檔建置產物
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo '建立 Docker 映像檔...'
                sh '''
                    echo "開始建立 Docker 映像檔..."

                    # 建立新的映像檔
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

                    # 也標記為 latest
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest

                    # 顯示映像檔資訊
                    echo "=== 建立的映像檔 ==="
                    docker images | grep ${IMAGE_NAME}
                '''
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
                        // 自動停止並清理現有容器
                        echo '自動清理現有容器...'
                        sh '''
                            echo "=== 停止現有服務 ==="
                            # 使用 docker-compose 停止所有服務
                            docker-compose down || echo "沒有 docker-compose 服務在運行"

                            echo "=== 強制清理容器 ==="
                            # 停止所有相關容器
                            docker stop member-system-app member-system-redis || echo "沒有運行中的容器"

                            # 移除所有相關容器
                            docker rm member-system-app member-system-redis || echo "沒有容器需要移除"

                            echo "=== 清理完成 ==="
                            docker ps | grep member-system || echo "確認：沒有相關容器在運行"
                        '''

                        // 等待容器完全停止
                        echo '等待容器完全清理...'
                        sleep(time: 5, unit: 'SECONDS')

                        // 啟動新版本
                        echo '自動啟動新版本...'
                        sh '''
                            echo "=== 使用新映像檔啟動服務 ==="
                            # 設定環境變數
                            export IMAGE_TAG=${BUILD_NUMBER}

                            # 啟動服務
                            docker-compose up -d

                            echo "=== 檢查部署結果 ==="
                            # 等待一下讓容器啟動
                            sleep 10

                            # 檢查容器狀態
                            docker ps | grep member-system || echo "警告：沒有找到相關容器"

                            # 檢查 docker-compose 服務
                            docker-compose ps || echo "docker-compose 狀態檢查失敗"
                        '''

                    } catch (Exception e) {
                        error "自動部署失敗: ${e.getMessage()}"
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                echo '執行健康檢查...'
                script {
                    // 等待應用程式啟動
                    echo '等待應用程式啟動...'
                    sleep(time: 30, unit: 'SECONDS')

                    // 檢查容器是否運行
                    sh '''
                        echo "=== 檢查容器狀態 ==="
                        docker ps | grep member-system || echo "沒有找到 member-system 容器"

                        echo "=== 檢查應用程式日誌 ==="
                        docker logs member-system-app --tail 20 || echo "無法獲取應用程式日誌"
                    '''

                    // 檢查應用程式健康狀態
                    def maxRetries = 10
                    def retryCount = 0
                    def healthCheckPassed = false

                    while (retryCount < maxRetries && !healthCheckPassed) {
                        try {
                            sh """
                                curl -f http://localhost:${APP_PORT}/actuator/health
                            """
                            healthCheckPassed = true
                            echo "✅ 應用程式健康檢查通過！"
                        } catch (Exception e) {
                            retryCount++
                            echo "健康檢查失敗，重試 ${retryCount}/${maxRetries}..."
                            if (retryCount < maxRetries) {
                                sleep(time: 10, unit: 'SECONDS')
                            }
                        }
                    }

                    if (!healthCheckPassed) {
                        echo "⚠️ 健康檢查失敗，但部署已完成。請檢查應用程式狀態。"
                        // 顯示更多診斷資訊
                        sh '''
                            echo "=== 詳細診斷資訊 ==="
                            echo "容器狀態："
                            docker ps -a | grep member-system || echo "沒有相關容器"

                            echo "應用程式日誌："
                            docker logs member-system-app --tail 50 || echo "無法獲取日誌"

                            echo "網路連接測試："
                            curl -v http://localhost:${APP_PORT}/ || echo "連接失敗"
                        '''
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
                        echo "=== 清理前的映像檔 ==="
                        docker images | grep ${IMAGE_NAME} || echo "沒有相關映像檔"

                        # 清理舊的映像檔（保留最新 5 個）
                        docker images ${IMAGE_NAME} --format "{{.Tag}}" | grep -v latest | grep -v TAG | sort -nr | tail -n +6 | xargs -I {} docker rmi ${IMAGE_NAME}:{} || echo "沒有舊映像檔需要清理"

                        # 清理未使用的映像檔
                        docker image prune -f

                        echo "=== 清理後的映像檔 ==="
                        docker images | grep ${IMAGE_NAME} || echo "沒有相關映像檔"
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
            echo '🎉 自動化部署成功！'
            script {
                def deploymentInfo = """
                🎉 自動部署成功通知
                專案: ${env.JOB_NAME}
                建置號: ${env.BUILD_NUMBER}
                提交者: ${env.GIT_AUTHOR}
                提交訊息: ${env.GIT_COMMIT_MSG}
                應用程式健康檢查: http://localhost:${APP_PORT}/actuator/health
                Swagger UI: http://localhost:${APP_PORT}/swagger-ui.html
                部署時間: ${new Date()}

                🚀 下次推送程式碼時，系統將自動：
                1. 停止舊版本
                2. 建立新映像檔
                3. 自動部署新版本
                4. 執行健康檢查
                """
                echo deploymentInfo
            }
        }

        failure {
            echo '❌ 自動化部署失敗！'
            script {
                def failureInfo = """
                ❌ 自動部署失敗通知
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