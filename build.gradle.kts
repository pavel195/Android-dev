import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.register
import org.gradle.api.tasks.JavaExec
//скрипт сборки Gradle,
plugins {
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.googlecode.lanterna:lanterna:3.1.2")
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
registerRunTask("runOopShop", "tasks.oop.shop.app.OopShopAppKt")
registerRunTask("runTeachersDelegation", "tasks.delegation.teachers.app.TeachersAppKt")
registerRunTask("runPomodoro", "tasks.coroutines.pomodoro.app.PomodoroAppKt")


