"""Execute a command."""

import sys

import anyio

import dagger


async def test():
    async with dagger.Connection(dagger.Config(log_output=sys.stderr)) as client:
        build_container = (
            client.container()
            .from_("maven:3.8.3-openjdk-17")
            .with_mounted_directory("/src", client.host().directory("."))
            .with_workdir("/src")
            .with_exec(["mvn", "-B", "package"])
        )
        
        # Do Build
        outcome = await build_container.exit_code()

        if outcome != 0:
            print("Failure: Build")
            return
        
        # Do Test
        build_container = build_container.with_exec(["mvn", "-B", "integration-test", "-P", "failsafe"])

        outcome = await build_container.exit_code()

        if outcome != 0:
            print("Failure: Integration Test")
            return
        
        # Do Prepare Artifact
        artifact = build_container\
            .with_exec(["mkdir", "/app"]) \
            .with_exec(["cp", "/src/userprofile-spring-runner/target/app.jar", "/app/app.jar"]) \
            .file("/app/app.jar")

        # Do E2E Test
        run_container = (
            client.container()
            .from_("eclipse-temurin:17-jdk-alpine")
            .with_file("/my_service/app.jar", artifact)
            .with_workdir("/my_service")
            .with_exec(["java", "--version"])
            .with_exec(["java", "-jar", "app.jar"])
            .with_exposed_port(8080)
        )

        test_runner = (
            client.container()
            .from_("kong/inso")
            .with_mounted_directory("/var/temp", client.host().directory("."))
            .with_service_binding("service", run_container)
            .with_exec(["run", "test", "User CRUD Test Suite", "-w", "/var/temp", "--env", "test"])
            #.with_exec(["wget", "-O-", "http://service:8080/actuator/health"])
        )

        outcome = await test_runner.stdout()
        
    print(f"Hello from Dagger and {outcome}")


if __name__ == "__main__":
    anyio.run(test)