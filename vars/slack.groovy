def call(String buildStatus = 'STARTED') {
    buildStatus = buildStatus ?: 'SUCCESS'

    def color
    def emoji

    if (buildStatus == 'SUCCESS') {
        color = '#2EB67D'          // Slack green
        emoji = ':white_check_mark:'
    } else if (buildStatus == 'UNSTABLE') {
        color = '#ECB22E'          // Slack yellow
        emoji = ':warning:'
    } else {
        color = '#E01E5A'          // Slack red
        emoji = ':x:'
    }

    def duration = currentBuild.durationString.replace(' and counting', '')

    def attachments = [
        [
            "color": color,
            "blocks": [
                [
                    "type": "header",
                    "text": [
                        "type": "plain_text",
                        "text": "K8S Deployment - ${deploymentName} Pipeline Status ${emoji}",
                        "emoji": true
                    ]
                ],
                [
                    "type": "section",
                    "fields": [
                        [
                            "type": "mrkdwn",
                            "text": "*Job Name:*\n${env.JOB_NAME}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Build Number:*\n${env.BUILD_NUMBER}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Duration:*\n${duration}"
                        ]
                    ],
                    "accessory": [
                        "type": "image",
                        "image_url": "https://raw.githubusercontent.com/sidd-harth/devsecops-k8s-demo/main/slack-emojis/jenkins.png",
                        "alt_text": "Jenkins Icon"
                    ]
                ],
                [
                    "type": "section",
                    "text": [
                        "type": "mrkdwn",
                        "text": "*Failed Stage Name:* `${env.failedStage ?: 'None'}'"
                    ],
                    "accessory": [
                        "type": "button",
                        "text": [
                            "type": "plain_text",
                            "text": "Jenkins Build URL",
                            "emoji": true
                        ],
                        "url": "${env.BUILD_URL}",
                        "action_id": "button-action"
                    ]
                ],
                [
                    "type": "divider"
                ],
                [
                    "type": "section",
                    "fields": [
                        [
                            "type": "mrkdwn",
                            "text": "*Kubernetes Deployment Name:*\n${deploymentName}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Node Port:*\n32564"
                        ]
                    ],
                    "accessory": [
                        "type": "image",
                        "image_url": "https://raw.githubusercontent.com/sidd-harth/devsecops-k8s-demo/main/slack-emojis/k8s.png",
                        "alt_text": "Kubernetes Icon"
                    ]
                ],
                [
                    "type": "section",
                    "text": [
                        "type": "mrkdwn",
                        "text": "*Kubernetes Node:* `controlplane`"
                    ],
                    "accessory": [
                        "type": "button",
                        "text": [
                            "type": "plain_text",
                            "text": "Application URL",
                            "emoji": true
                        ],
                        "url": "${applicationURL}:32564",
                        "action_id": "button-action"
                    ]
                ],
                [
                    "type": "divider"
                ],
                [
                    "type": "section",
                    "fields": [
                        [
                            "type": "mrkdwn",
                            "text": "*Git Commit:*\n${GIT_COMMIT}"
                        ],
                        [
                            "type": "mrkdwn",
                            "text": "*Previous Success Commit:*\n${GIT_PREVIOUS_SUCCESSFUL_COMMIT ?: 'N/A'}"
                        ]
                    ],
                    "accessory": [
                        "type": "image",
                        "image_url": "https://raw.githubusercontent.com/sidd-harth/devsecops-k8s-demo/main/slack-emojis/github.png",
                        "alt_text": "GitHub Icon"
                    ]
                ],
                [
                    "type": "section",
                    "text": [
                        "type": "mrkdwn",
                        "text": "*Git Branch:* `${GIT_BRANCH}`"
                    ],
                    "accessory": [
                        "type": "button",
                        "text": [
                            "type": "plain_text",
                            "text": "GitHub Repo URL",
                            "emoji": true
                        ],
                        "url": "${env.GIT_URL ?: '#'}",
                        "action_id": "button-action"
                    ]
                ]
            ]
        ]
    ]

    slackSend(iconEmoji: emoji, attachments: attachments)
}
