#!/usr/bin/env bash
set -euo pipefail

./gradlew \
    :common:publishOSSRHPublicationToProjectLocalRepository \
    :picocli:publishOSSRHPublicationToProjectLocalRepository \
    :edhl:publishOSSRHPublicationToProjectLocalRepository

./gradlew \
    :common:zipMavenCentralPortalPublication \
    :picocli:zipMavenCentralPortalPublication \
    :edhl:zipMavenCentralPortalPublication

./gradlew \
    :common:releaseMavenCentralPortalPublication \
    :picocli:releaseMavenCentralPortalPublication \
    :edhl:releaseMavenCentralPortalPublication
