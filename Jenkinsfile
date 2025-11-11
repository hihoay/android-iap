 pipeline {
     agent any
     environment {
        SITE='https://production.hihoay.com'
        FILE_SERVICE='https://production.hihoay.com/file/service'
        DISCORD_WEBHOOK_PRODUCTION="https://discord.com/api/webhooks/1270484334364852304/5VKRAkcxI7iaG3UPJPlw8uGDVhKz-pNnBJSFLwGGSLYz8Atfi5RECD1D-9BOzatw-FpQ"
        DISCORD_WEBHOOK_TESTING="https://discord.com/api/webhooks/1348843757453447261/bSPsXUgwaWVLFUa6r1H-W-rtbn8YJ1oMgaM1r0AZx1kEby3_87B_JbHqg_nSWGYsIGOh"
        DISCORD_WEBHOOK='https://discord.com/api/webhooks/1381091346936234015/-JuXIE7Q5XnYthzoPWPtBjhRlUF6Ba1uvWzFvotDEV7DlrSk4_Td2vmODHJeTS8s7SvL'
        DISCORD_WEBHOOK_GITHUB='https://discord.com/api/webhooks/1428711017701179402/PGmnaTPMikQleu7kowVuBa76hZUJixObuNOIT2Bc8UOoZLH08VI0fcw-6yWVVqs1_Omt'
     }
     stages {
          stage('Run Builder Script') {
              steps {
                  sendDiscordNotification("${JOB_NAME}: [${BUILD_DISPLAY_NAME} Starting](${BUILD_URL}/pipeline-console)")
                  sh 'bash .taymay/builder.sh app iap'
              }
          }
     }
     post {

        always {
            cleanWs()
        }

        success {
             script {
                 sendDiscordNotification("${JOB_NAME}: [${BUILD_DISPLAY_NAME} Success](${BUILD_URL}/pipeline-console)")
             }
         }

         failure {
             script {
                 sendDiscordNotification("${JOB_NAME}: [${BUILD_DISPLAY_NAME} Failed](${BUILD_URL}/pipeline-console)")
             }
         }
     }
 }

void sendDiscordNotification(String content) {
    def payload = """
    {
        "username": "${GIT_AUTHOR_NAME}",
        "avatar_url": "https://mirrors.tuna.tsinghua.edu.cn/jenkins/art/jenkins-logo/256x256/headshot.png",
        "content": "${content}"
    }
    """
    sh "curl -H 'Content-Type: application/json' -X POST -d '${payload}' ${DISCORD_WEBHOOK}"
}

