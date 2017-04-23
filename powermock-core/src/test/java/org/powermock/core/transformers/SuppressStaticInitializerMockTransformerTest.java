/*
 *
 *   Copyright 2017 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.powermock.core.transformers;


import org.junit.Test;
import org.junit.runners.Parameterized;
import org.powermock.core.MockRepository;
import org.powermock.core.transformers.javassist.SuppressStaticInitializerMockTransformer;
import org.powermock.reflect.Whitebox;
import powermock.test.support.MainMockTransformerTestSupport.StaticInitialization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeThat;

public class SuppressStaticInitializerMockTransformerTest extends AbstractBaseMockTransformerTest {
    
    
    @Parameterized.Parameters(name = "strategy: {0}, transformer: {1}")
    public static Iterable<Object[]> data() {
        return MockTransformerTestHelper.createTransformerTestData(
            SuppressStaticInitializerMockTransformer.class
        );
    }
    
    public SuppressStaticInitializerMockTransformerTest(final TransformStrategy strategy, final MockTransformerChain mockTransformerChain) {
        super(strategy, mockTransformerChain);
    }
    
    @Test
    public void should_suppress_static_initialization_if_class_is_added_to_mock_repository() throws ClassNotFoundException {
        assumeThat(strategy, equalTo(TransformStrategy.CLASSLOADER));
        
        String className = StaticInitialization.class.getName();
    
        MockRepository.addSuppressStaticInitializer(className);
    
        Class<?> clazz = loadWithMockClassLoader(className);
        
        Object value = Whitebox.getInternalState(clazz, "value");
        
        assertThat(value)
            .as("Value not initialized")
            .isNull();
    }
    
    @Test
    public void should_not_suppress_static_initialization_if_class_is_not_added_to_mock_repository() throws ClassNotFoundException {
        assumeThat(strategy, equalTo(TransformStrategy.CLASSLOADER));
    
        Class<?> clazz = loadWithMockClassLoader(StaticInitialization.class.getName());
        
        Object value = Whitebox.getInternalState(clazz, "value");
        
        assertThat(value)
            .as("Value not initialized")
            .isNotNull();
    }
}