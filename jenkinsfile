@Library('slack') _

/////// ******************************* Code for fectching Failed Stage Name ******************************* ///////
import io.jenkins.blueocean.rest.impl.pipeline.PipelineNodeGraphVisitor
import io.jenkins.blueocean.rest.impl.pipeline.FlowNodeWrapper
import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper
import org.jenkinsci.plugins.workflow.actions.ErrorAction

@NonCPS
List<Map> getStageResults(RunWrapper build) {
    def visitor = new PipelineNodeGraphVisitor(build.rawBuild)
    def stages = visitor.pipelineNodes.findAll { it.type == FlowNodeWrapper.NodeType.STAGE }

    return stages.collect { stage ->
        def errorActions = stage.getPipelineActions(ErrorAction)
        def errors = errorActions?.collect { it.error }?.unique()

        return [
            id             : stage.id,
            failedStageName: stage.displayName,
            result         : "${stage.status.result}",
            errors         : errors
        ]
    }
}

@NonCPS
List<Map> getFailedStages(RunWrapper build) {
    return getStageResults(build).findAll { it.result == 'FAILURE' }
}
/////// ******************************* Code for fectching Failed Stage Name ******************************* ///////

pipeline {
    agent any

        stage('Testing Slack - 1') {
            steps {
                sh 'exit 0'
            }
        }

        stage('Testing Slack - Error Stage') {
            steps {
                sh 'exit 0'
            }
        }
    }

    post {
        success {
            script {
                env.failedStage = "none"
                env.emoji = ":white_check_mark: :tada: :thumbsup_all:"
                slack currentBuild.result
            }
        }

        failure {
            script {
                def failedStages = getFailedStages(currentBuild)
                env.failedStage = failedStages.failedStageName
                env.emoji = ":x: :red_circle: :sos:"
                slack currentBuild.result
            }
        }
    }
}
