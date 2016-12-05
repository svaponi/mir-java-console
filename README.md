# mir-java-console
A Java interactive console, simulates the typical console of interpreted languages like Python, Ruby, etc..

## Maven dependecy
Use [jitpack.io](https://jitpack.io/) as package repository for Git.

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
	...
</repositories>

<dependencies>
	<dependency>
		<groupId>com.github.svaponi</groupId>
		<artifactId>mir-java-console</artifactId>
		<version>master-SNAPSHOT</version>
	</dependency>
	...
</dependencies>
```

## How to
Example on how to start the console.

```bash
git clone https://github.com/svaponi/mir-java-console

cd mir-java-console

mvn clean compile assembly:single

user@machine:/path/to/mir-java-console$ java -jar target/mir-java-console-1.1.0-jar-with-dependencies.jar
13:35:14.006 INFO  it.miriade.console.Main - Version        1.1.0
CONSOLE > for(int i=0; i<10; i++)
CONSOLE > syso Math.random() * i
CONSOLE > return
0.0
0.587068054918665
0.7806313085633403
0.2779141668148323
0.45419570657133645
4.759717465831881
0.6570029305988665
3.7337484623850576
3.4054928470588814
0.40912647921279865

```

Use `exit` to exit the console.

Have fun :)
