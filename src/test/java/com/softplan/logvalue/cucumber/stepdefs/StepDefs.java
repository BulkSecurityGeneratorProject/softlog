package com.softplan.logvalue.cucumber.stepdefs;

import com.softplan.logvalue.LogValueApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = LogValueApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
