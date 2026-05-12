package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/willYouSurvive")
public class SurvivalCheck extends HttpServlet {
    
    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse res) throws IOException {

        Integer teaCups = parseNonNegativeInt(req.getParameter("teaCups"));
        Integer sleepHours = parseNonNegativeInt(req.getParameter("sleepHours"));
        Integer backlogCount = parseNonNegativeInt(req.getParameter("backlogCount"));
        Integer leetCodeProblems = parseNonNegativeInt(req.getParameter("leetCodeProblems"));
        Integer lastAssessmentProblems = parseNonNegativeInt(req.getParameter("lastAssessmentProblems"));

        if (teaCups == null || sleepHours == null || backlogCount == null || leetCodeProblems == null || lastAssessmentProblems == null) {
            res.setStatus(400);
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.println("{\"error\": \"All fields are required and must be non-negative integers.\"}");
            return;
        }

        double survivalPercentage = calculateSurvivalPercentage(
                teaCups,
                sleepHours,
                backlogCount,
                leetCodeProblems,
                lastAssessmentProblems);

        String message = getMessage(survivalPercentage);

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        out.println("{\"survivalPercentage\": " + survivalPercentage + ", \"message\": \"" + message + "\"}");
        return;

    }

    private Integer parseNonNegativeInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed < 0 ? null : parsed;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private double calculateSurvivalPercentage(int teaCups, int sleepHours, int backlogCount,
            int leetCodeProblems, int lastAssessmentProblems) {
                
        double percentage = 100.0;

        if( teaCups > 2) {
            percentage -= (teaCups - 2) * 5;
        }
        if( sleepHours < 5) {
            percentage -= (5 - sleepHours) * 10;
        }
        if( backlogCount > 1) {
            percentage -= backlogCount * 10;
        }
        if( leetCodeProblems < 100) {
            percentage -= (100 - leetCodeProblems) * 2;
        }
        if( lastAssessmentProblems < 5) {
            percentage -= (5 - lastAssessmentProblems) * 3;
        }

        return percentage;
    }

    private String getMessage(double percentage) { 
        if (percentage >= 80) {
            return "Vera maari, Mass nov neenga!";
        } else if (percentage >= 50) {
            return "Not bad, but podhadhu";
        } else if( percentage >= 20) {
            return "Enna bro, kashta kaalam";
        } else {
            return "Better pack it up and go home. Time to rethink your life choices";
        }
    }

}
