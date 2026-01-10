package com.project.realhealthbuddy;

import java.util.Random;

public class HealthTipsProvider {

    private static final String[] TIPS = {

            "Drink a glass of water after waking up.",
            "Stretch your body for 5 minutes every morning.",
            "Take slow deep breaths when feeling stressed.",
            "Avoid using your phone right after waking up.",
            "A short walk after meals helps digestion.",
            "Keep your posture straight while sitting.",
            "Limit sugary snacks during the day.",
            "Sleep at the same time every night.",
            "Reduce screen brightness at night.",
            "Drink water before feeling thirsty.",
            "Take breaks if you sit for long hours.",
            "Wash your hands regularly.",
            "Spend a few minutes in sunlight daily.",
            "Eat slowly and chew your food well.",
            "Avoid heavy meals late at night.",
            "Keep your room well ventilated.",
            "Do light stretching before bed.",
            "Reduce caffeine intake in the evening.",
            "Stay hydrated throughout the day.",
            "Practice mindful breathing for calmness.",

            "Stand up and move every 30 minutes.",
            "Listen to calming music to relax.",
            "Keep a consistent sleep routine.",
            "Limit junk food intake.",
            "Smile more to reduce stress naturally.",
            "Drink warm water in the morning.",
            "Take stairs instead of elevators when possible.",
            "Avoid skipping meals.",
            "Spend time away from screens daily.",
            "Maintain a clean sleeping environment.",
            "Practice gratitude before sleeping.",
            "Avoid late-night snacking.",
            "Keep your phone away while sleeping.",
            "Stretch your neck and shoulders gently.",
            "Avoid loud sounds before bed.",
            "Take a moment to relax your mind.",
            "Stay active even with small movements.",
            "Reduce salt intake when possible.",
            "Take deep breaths before reacting.",
            "Spend time with positive people."
    };

    public static String getRandomTip() {
        return TIPS[new Random().nextInt(TIPS.length)];
    }
}
