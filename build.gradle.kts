import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.register
import org.gradle.api.tasks.JavaExec

plugins {
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

val sourceSets = the<SourceSetContainer>()
val mainRuntime = sourceSets.named("main").get().runtimeClasspath

fun registerRunTask(taskName: String, mainClassName: String) =
    tasks.register<JavaExec>(taskName) {
        group = "application"
        description = "Run $mainClassName"
        classpath = mainRuntime
        mainClass.set(mainClassName)
        standardInput = System.`in`
    }

// Run-задачи для всех исполняемых файлов
registerRunTask("runSumEvenDigits", "tasks.SumEvenDigitsKt")
registerRunTask("runHofSumFunctions", "tasks.hof.SumFunctionsKt")
registerRunTask("runNextDay", "tasks.whenops.NextDayKt")
registerRunTask("runFASumEvenDigits", "tasks.functional.SumEvenDigitsOneLinerKt")
registerRunTask("runFAOddWordFirstChar", "tasks.functional.FirstCharOfFirstMaxOddWordKt")
registerRunTask("runFAArrayFromPairs", "tasks.functional.ArrayFromPairsKt")
registerRunTask("runFABitwiseAndPenultimate", "tasks.functional.BitwiseAndPenultimateKt")
registerRunTask("runFATopStudents", "tasks.functional.TopStudentsKt")
registerRunTask("runFAFibonacci", "tasks.functional.FibonacciByIndexKt")
registerRunTask("runToStringSafeDemo", "tasks.whenops.ToStringSafeKt")


