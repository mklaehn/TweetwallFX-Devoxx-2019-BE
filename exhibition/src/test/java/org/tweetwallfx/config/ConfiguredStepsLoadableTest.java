/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 TweetWallFX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.tweetwallfx.config;

import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.tweetwallfx.stepengine.api.Step;
import org.tweetwallfx.stepengine.api.config.StepEngineSettings;

@RunWith(Parameterized.class)
public class ConfiguredStepsLoadableTest {

    @Rule
    public TestName testName = new TestName();

    @Parameterized.Parameter(0)
    public String stepName;

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> parameters() {
        return Configuration.getInstance()
                .getConfigTyped("stepEngine", StepEngineSettings.class)
                .getSteps()
                .stream()
                .map(StepEngineSettings.StepDefinition::getStepClassName)
                .distinct()
                .sorted()
                .map(n -> new Object[]{n})
                .collect(Collectors.toList());
    }

    @Before
    public void before() {
        System.out.println("#################### START: " + testName.getMethodName() + " ####################");
    }

    @After
    public void after() {
        System.out.println("####################   END: " + testName.getMethodName() + " ####################");
    }

    @Test
    public void checkTestCase() throws Exception {
        final Set<String> availableStepFactories = new HashSet<>();
        for (final Step.Factory o : ServiceLoader.load(Step.Factory.class)) {
            availableStepFactories.add(o.getStepClass().getName());
        }

        Assert.assertThat(
                availableStepFactories,
                CoreMatchers.hasItem(stepName));
    }
}
