package com.github.va1m.shopping.integration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** Allows to run tests in correct order */
@Suite.SuiteClasses({
    TestAppendingLists.class,
    TestGettingLists.class,
    TestUpdatingLists.class,
    TestDeletingLists.class})
@RunWith(Suite.class)
public class OrderedTestSuite {
}
