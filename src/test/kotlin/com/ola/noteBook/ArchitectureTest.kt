package com.ola.noteBook
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.GeneralCodingRules
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Service

@AnalyzeClasses(packages=["com.ola.noteBook"])
class ArchitectureTest {
    @ArchTest
    val `controllers should not know about repositories` = noClasses()
        .that().haveSimpleNameEndingWith("Controller").should()
        .dependOnClassesThat().haveSimpleNameEndingWith("Repository")

    @ArchTest
    val `models should be independent` = noClasses().that().areAnnotatedWith(Document::class.java).should()
        .dependOnClassesThat().haveSimpleNameEndingWith("Controller")
        .orShould().dependOnClassesThat().haveSimpleNameEndingWith("Repository")
        .orShould().dependOnClassesThat().haveSimpleNameEndingWith("Service")

    @ArchTest
    val `controllers should have Controller suffix` = classes().that().areAnnotatedWith(RestController::class.java).should().haveSimpleNameEndingWith("Controller")
    @ArchTest
    val `services should have Service suffix` = classes().that().areAnnotatedWith(Service::class.java).should().haveSimpleNameEndingWith("Service")
    @ArchTest
    val `no classes should use field injection` = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
    @ArchTest
    val `features should be independent and not have cyclic dependencies` = slices()
        .matching("com.ola.noteBook.(*)..").should().beFreeOfCycles()
}
