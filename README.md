# Ethelred Util

Various general utility classes I've accumulated over the years.

![Java CI with Gradle](https://github.com/edward3h/ethelred_util/workflows/Java%20CI%20with%20Gradle/badge.svg)

## Modules

### Common
General purpose pure Java classes.

`org.ethelred.util:common:2.0`

[![javadoc](https://javadoc.io/badge2/org.ethelred.util/common/javadoc.svg)](https://javadoc.io/doc/org.ethelred.util/common)

### Console
Terminal/console utilities. Has native dependencies _that have only been tested on Linux._

`org.ethelred.util:console:2.0`

[![javadoc](https://javadoc.io/badge2/org.ethelred.util/console/javadoc.svg)](https://javadoc.io/doc/org.ethelred.util/console)

### Groovy
Write data in a tabular format in your Groovy code.
Years since I wrote this: I'm not sure how useful it is anymore.

`org.ethelred.util:groovy:2.0`

### Picocli _coming soon_
Integrate environment variables and config files with your Picocli options.

## Publishing

Because I can never remember the command.

`gw publishToSonatype closeAndReleaseSonatypeStagingRepository`
