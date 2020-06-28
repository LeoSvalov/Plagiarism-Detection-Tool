# Plagiarism Detection Tool
The Java client that brings reports from 2 well-known plagiarism tools: MOOS and JPlag \
To make it work, you need to set up both tools locally on your machine:
  - for JPlag, just ```.jar``` file(that already in repo) needs to be stored in the same folder, so, just clone the repo with it
  - for MOSS, you need to register and setup the tool manually \
    [link](http://theory.stanford.edu/~aiken/moss/) with register inistructions \
    [link](https://stackoverflow.com/questions/20905330/how-do-i-use-the-moss-script) with setting instructioins \
     **note** that the script (**moss.pl**) needs to be stored in the project folder

# Junit tests #
In Junit, tests are written in java classes. Each java class contains methods. Those methods are responsible for running tests in a specific way determined by the annotation declared before the methods. For instance `@beforeAll` describes a testing method that will run before all the other tests.

## Here's a list of the popular annotations used for testing: ##
- `@BeforeAll`: denotes that the annotated method will be executed before all test methods in the current class
- `@BeforeEach`: denotes that the annotated method will be executed before each test method
- `@test`: denotes a regular test method
- `@AfterEach`: denotes that the annotated method will be executed after each test method
- `@AfterAll`: denotes that the annotated method will be executed after all test methods in the current class
- `@Disable`: it is used to disable a test class or method
## Other annotations ##
- `@DisplayName`: defines custom display name for a test class or a test method
- `@Tag`: declares tags for filtering tests
## Writing tests ##


In order to write a Junit tests we need to use either maven or gradle with Java.
In maven, we need to use the Junit dependencies in *pom.xml*:

**Here we also need to use Reflections library to fetch the java programs that will be tested**

	<dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>${junit.jupiter.version}</version>
        <scope>test</scope>
    </dependency>
	<dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>${junit.jupiter.version}</version>
        <scope>test</scope>
    </dependency>
	<dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-launcher</artifactId>
        <version>1.2.0</version>
    </dependency>
    <dependency>
	    <groupId>org.junit.jupiter</groupId>
	    <artifactId>junit-jupiter-params</artifactId>
	    <version>5.4.2</version>
	    <scope>test</scope>
	</dependency>
    <dependency>
	    <groupId>org.reflections</groupId>
	    <artifactId>reflections</artifactId>
	    <version>0.9.12</version>
	</dependency>


Create a test class with the following being imported:

	import org.junit.jupiter.api.AfterAll;
	import org.junit.jupiter.api.BeforeAll;
	import org.junit.jupiter.api.DisplayName;
	import org.junit.jupiter.api.Tag;
	import org.junit.jupiter.api.Test;
	import org.junit.platform.engine.discovery.PackageNameFilter;
	import org.reflections.Reflections;

Then start writing the Junit tests using the aforementioned annotations.

## Fetching all classes that inherit a specific class ##

To fetch all classes that extend the Class *parent* and are in a package that has its name starting with the prefix *PackageNamePrefix*

Here (5th line) *Answer* is the parent; make sure to change it.

    Package[] pkgs = Package.getPackages();
	for(Package pkg: pkgs) {
		if(!pkg.getName().startsWith(PackageNamePrefix)) continue;
		Reflections reflections = new Reflections(pkg.getName());
		Set<Class<? extends Answer>> clss = reflections.getSubTypesOf(parent);
		for(Class cls: clss) classes.add(cls);
	}

Then you can run the tests using the IDE running functionality or using another Java program.

# Running Junit Tests using a Java program #

In order to run a Junit testing class using a java program:

First, Import the Junit core

    import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
	import org.junit.platform.launcher.Launcher;
	import org.junit.platform.launcher.LauncherDiscoveryRequest;
	import org.junit.platform.launcher.TestPlan;
	import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
	import org.junit.platform.launcher.core.LauncherFactory;
	import org.junit.platform.engine.discovery.DiscoverySelectors;

Second, create a *void function* that will run the Junit testing class when invoked.
Here *AnswersTest* is the required Junit testing class, and it should be imported as well.
	
	public class Testing {
		static SummaryGeneratingListener listener = new SummaryGeneratingListener();
		public static void main(String[] args) {
			....
		}
		...
		public static void startTesting() {
			LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
					.request()
					.selectors(DiscoverySelectors.selectClass(AnswersTest.class))
					.build();
			Launcher launcher = LauncherFactory.create();
			TestPlan testPlan = launcher.discover(request);
			launcher.registerTestExecutionListeners(listener);
			launcher.execute(request);
		}
	}

Finally, you can invoke *startTesting()* from anywhere in a java program. For example:

	public static void main(String[] args) {
		Scanner io = new Scanner(System.in);
		String in = io.next();
		if(in.toLowerCase().equals("yes"))
			startTesting();
	}
