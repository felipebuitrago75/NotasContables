#!groovy
@Library ('workflow-spring') _
pipeline {
    agent any
    triggers {
         pollSCM('') 
         
    }
    stages {
        stage('EXECUTION') {
            steps {
                script {
                    spring {       
                        
                        verbosity = 2
                        country = "co"
                        group = "notas-contables"
                        revision = "feature/NO-ISSUE-Deploy"
                        uuaa = "BBVACNC"
                    }    
                }    
            }
        }
    }
}