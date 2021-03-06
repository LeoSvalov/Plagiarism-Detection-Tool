package io.validation;

import java.util.Scanner;

import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import io.validation.AnswersTest;

public class Testing {
	static SummaryGeneratingListener listener = new SummaryGeneratingListener();
	public static void main(String[] args) {
		runOne();
	}
	
	public static void runOne() {
		LauncherDiscoveryRequest request =
				LauncherDiscoveryRequestBuilder
				.request()
				.selectors(DiscoverySelectors.selectClass(AnswersTest.class))
				.build();
		Launcher launcher = LauncherFactory.create();
		TestPlan testPlan = launcher.discover(request);
		launcher.registerTestExecutionListeners(listener);
		launcher.execute(request);
	}

}
