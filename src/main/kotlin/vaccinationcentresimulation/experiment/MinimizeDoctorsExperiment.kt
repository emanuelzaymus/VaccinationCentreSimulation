package vaccinationcentresimulation.experiment

object MinimizeDoctorsExperiment {

    fun start() {
        for (admin in 13..15) {
            for (doctors in 20..22) {
                for (nurses in 5..7) {

                    val sim = VaccinationCentreExperiment(
                        replicationsCount = 200,
                        numberOfPatientsPerReplication = 2500,
                        numberOfAdminWorkers = admin,
                        numberOfDoctors = doctors,
                        numberOfNurses = nurses,
                        withAnimation = false
                    )
                    sim.start()

//                    println("Admin: $admin Doctors: $doctors Nurses: $nurses")
//                    println("Reg. q. length:   " + sim.allRegistrationQueueLengths.getAverage())
//                    println("Reg. q. waiting:  " + sim.allRegistrationWaitingTimes.getAverage())
//                    println("Admin workers:    " + sim.allAdminWorkersWorkloads.getAverage())
//
//                    println("Exam. q. length:  " + sim.allExaminationQueueLengths.getAverage())
//                    println("Exam. q. waiting: " + sim.allExaminationWaitingTimes.getAverage())
//                    println("Doctors:          " + sim.allDoctorsWorkloads.getAverage())
//
//                    println("Vac. q. length:   " + sim.allVaccinationQueueLengths.getAverage())
//                    println("Vac. q. waiting:  " + sim.allVaccinationWaitingTimes.getAverage())
//                    println("Nurses:           " + sim.allNursesWorkloads.getAverage())
//
//                    println("Waiting room:     " + sim.allWaitingPatientsCounts.getAverage())
//                    println("Lower bound:      " + sim.allWaitingPatientsCounts.lowerBoundOfConfidenceInterval())
//                    println("Upper bound:      " + sim.allWaitingPatientsCounts.upperBoundOfConfidenceInterval())
//                    println()

                    val regLen = sim.allRegistrationQueueLengths.getAverage()
                    val relWait = sim.allRegistrationWaitingTimes.getAverage()
                    val adm = sim.allAdminWorkersWorkloads.getAverage()

                    val exaLen = sim.allExaminationQueueLengths.getAverage()
                    val exaWait = sim.allExaminationWaitingTimes.getAverage()
                    val doc = sim.allDoctorsWorkloads.getAverage()

                    val vacLen = sim.allVaccinationQueueLengths.getAverage()
                    val vacWait = sim.allVaccinationWaitingTimes.getAverage()
                    val nur = sim.allNursesWorkloads.getAverage()

                    println("$regLen\t$relWait\t$exaLen\t$exaWait\t$vacLen\t$vacWait\t$adm\t$doc\t$nur")
                }
            }
        }
    }

}