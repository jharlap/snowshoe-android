# Releasing new versions

This assumes that you have a `~/.gradle/gradle.properties` similar to that listed at the end of this document.

1. Bump the VERSION_NAME in gradle.properties to a non-SNAPSHOT version and VERSION_CODE to the next integer value.
2. git add gradle.properties && git commit -m 'Releasing version X.Y.Z' && git tag 'VERSION-X.Y.Z'
3. ./gradlew clean build uploadArchives
4. Bump the VERSION_NAME in gradle.properties to the next SNAPSHOT version and VERSION_CODE to the next integer value.
5. git add gradle.properties && git commit -m 'Version bump'
6. Log in to https://oss.sonatype.org/ and promote the release.

## ~/.gradle/gradle.properties

signing.keyId=YOUR_KEY_ID
signing.password=YOUR_PASSWORD
signing.secretKeyRingFile=/path/to/.gnupg/secring.gpg
NEXUS_USERNAME=YOUR_NEXUS_USERNAME
NEXUS_PASSWORD=YOUR_NEXUS_PASSWORD
