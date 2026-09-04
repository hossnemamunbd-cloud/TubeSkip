package com.example.util

object AppStrings {
    fun get(key: String, lang: String): String {
        val isBn = lang == "bn"
        return when (key) {
            "app_name" -> if (isBn) "টিউবস্কিপ - ভিডিও ব্লকার" else "TubeSkip - Video Blocker"
            "dashboard" -> if (isBn) "ড্যাশবোর্ড" else "Dashboard"
            "keywords" -> if (isBn) "কীওয়ার্ড" else "Keywords"
            "curated" -> if (isBn) "ব্লকিং প্যাক" else "Curated Packs"
            "history" -> if (isBn) "হিস্টোরি" else "History"
            "settings" -> if (isBn) "সেটিংস" else "Settings"

            // Service status
            "status_active" -> if (isBn) "অটো-স্কিপ সক্রিয়" else "Auto-Skip Active"
            "status_disabled" -> if (isBn) "সার্ভিস বন্ধ আছে" else "Service Disabled"
            "status_active_desc" -> if (isBn) "টিউবস্কিপ আপনার ইউটিউব ভিডিও ফিল্টার করছে।" else "TubeSkip is actively scanning & filtering YouTube."
            "status_disabled_desc" -> if (isBn) "ভিডিও স্কিপ করতে এক্সেসিবিলিটি সার্ভিস চালু করুন।" else "Enable Accessibility Service to allow auto-skipping."
            "btn_enable_service" -> if (isBn) "এক্সেসিবিলিটি চালু করুন" else "Enable Accessibility"
            "btn_permission_guide" -> if (isBn) "কীভাবে চালু করবেন?" else "How to enable?"

            // Stats
            "stat_total_skipped" -> if (isBn) "মোট স্কিপড" else "Total Skipped"
            "stat_shorts_skipped" -> if (isBn) "শর্টস স্কিপড" else "Shorts Skipped"
            "stat_videos_skipped" -> if (isBn) "ভিডিও স্কিপড" else "Videos Skipped"
            "stat_time_saved" -> if (isBn) "বাঁচানো সময়" else "Est. Time Saved"
            "time_mins" -> if (isBn) "মিনিট" else "mins"
            "time_hours" -> if (isBn) "ঘণ্টা" else "hours"
            "stat_active_keywords" -> if (isBn) "সক্রিয় কীওয়ার্ড" else "Active Rules"

            // Simulator
            "sim_title" -> if (isBn) "কীওয়ার্ড ম্যাচ টেস্ট করুন" else "Keyword Match Simulator"
            "sim_desc" -> if (isBn) "ইউটিউবের কোনো ভিডিও টাইটেল লিখে পরীক্ষা করুন এটি স্কিপ হবে কি না।" else "Type or paste a video title to see if it will be skipped."
            "sim_placeholder" -> if (isBn) "যেমন: দেখুন কি হলো শেষ পর্যন্ত…" else "e.g., You won't believe what happened..."
            "sim_btn_test" -> if (isBn) "টেস্ট করুন" else "Test Match"
            "sim_result_skipped" -> if (isBn) "অটো স্কিপ হবে!" else "Will AUTO-SKIP!"
            "sim_result_passed" -> if (isBn) "স্বাভাবিক চলবে (কোনো ম্যাচ নেই)" else "Will play normally (No match)"
            "sim_matched_with" -> if (isBn) "ম্যাচ করা কীওয়ার্ড:" else "Matched with:"

            // Keyword management
            "search_keywords" -> if (isBn) "কীওয়ার্ড খুঁজুন…" else "Search keywords…"
            "filter_all" -> if (isBn) "সব" else "All"
            "btn_add_keyword" -> if (isBn) "নতুন কীওয়ার্ড" else "Add Keyword"
            "dialog_add_title" -> if (isBn) "নতুন কীওয়ার্ড যোগ করুন" else "Add New Keyword"
            "dialog_edit_title" -> if (isBn) "কীওয়ার্ড সম্পাদনা" else "Edit Keyword"
            "label_keyword_text" -> if (isBn) "কীওয়ার্ড বা বাক্য" else "Keyword or Phrase"
            "hint_keyword_text" -> if (isBn) "যেমন: prank, spoiler, ক্লিকবেইট" else "e.g., prank, spoiler, clickbait"
            "label_category" -> if (isBn) "ক্যাটাগরি" else "Category"
            "label_match_type" -> if (isBn) "ম্যাচ টাইপ" else "Match Type"
            "match_contains" -> if (isBn) "যেকোনো অংশে থাকা (Contains)" else "Contains Word"
            "match_exact" -> if (isBn) "হুবহু এক (Exact Match)" else "Exact Match"
            "match_starts" -> if (isBn) "শুরুতে থাকা (Starts With)" else "Starts With"
            "match_regex" -> if (isBn) "রেগুলার এক্সপ্রেশন (Regex)" else "Regular Expression"
            "label_case_sensitive" -> if (isBn) "কেস সেনসিটিভ (ছোট/বড় হাতের অক্ষর পৃথক)" else "Case Sensitive"
            "btn_save" -> if (isBn) "সংরক্ষণ করুন" else "Save Keyword"
            "btn_cancel" -> if (isBn) "বাতিল" else "Cancel"
            "btn_delete" -> if (isBn) "মুছুন" else "Delete"
            "no_keywords_title" -> if (isBn) "কোনো কীওয়ার্ড নেই" else "No Keywords Added"
            "no_keywords_desc" -> if (isBn) "উপরে '+' বাটনে ক্লিক করে বা কিউরেটেড প্যাক থেকে কীওয়ার্ড যোগ করুন।" else "Tap '+' or enable a Curated Pack to add blocked words."

            // Curated packs
            "curated_title" -> if (isBn) "কিউরেটেড ব্লকিং প্যাকসমূহ" else "Curated Blocking Packs"
            "curated_subtitle" -> if (isBn) "এক ক্লিকে জনপ্রিয় ক্যাটাগরির অবাঞ্ছিত ভিডিও ব্লক করুন" else "One-tap enable pre-built filters for unwanted content"
            "pack_installed" -> if (isBn) "চালু আছে" else "Active"
            "btn_add_pack" -> if (isBn) "প্যাক যোগ করুন" else "Add Pack"
            "btn_remove_pack" -> if (isBn) "প্যাক সরান" else "Remove"

            // History / Logs
            "logs_title" -> if (isBn) "স্কিপ করা ভিডিও হিস্টোরি" else "Skipped Videos Log"
            "no_logs_title" -> if (isBn) "এখনো কোনো স্কিপ হিস্টোরি নেই" else "No Skip History Yet"
            "no_logs_desc" -> if (isBn) "ইউটিউবে ভিডিও দেখার সময় ম্যাচ হলে এখানে বিস্তারিত দেখতে পাবেন।" else "When YouTube videos are auto-skipped, logs appear here."
            "btn_clear_logs" -> if (isBn) "হিস্টোরি মুছুন" else "Clear History"

            // Settings
            "settings_general" -> if (isBn) "সাধারণ সেটিংস" else "General Settings"
            "settings_features" -> if (isBn) "ফিচার কন্ট্রোল" else "Features & Behavior"
            "settings_language" -> if (isBn) "অ্যাপের ভাষা (Language)" else "App Language"
            "lang_bengali" -> "বাংলা (Bengali)"
            "lang_english" -> "English"
            "settings_theme" -> if (isBn) "অ্যাপ থিম" else "Theme Mode"
            "theme_system" -> if (isBn) "সিস্টেম ডিফল্ট" else "System Default"
            "theme_dark" -> if (isBn) "ডার্ক মোড" else "Dark Mode"
            "theme_light" -> if (isBn) "লাইট মোড" else "Light Mode"
            "setting_skip_videos" -> if (isBn) "ফুল ভিডিও অটো-স্কিপ" else "Auto-Skip Standard Videos"
            "setting_skip_videos_desc" -> if (isBn) "নরমাল লং ভিডিওর টাইটেল ম্যাচ হলে পরবর্তী ভিডিও চালু করে" else "Skips long videos when title or description matches"
            "setting_skip_shorts" -> if (isBn) "শর্টস ভিডিও অটো-স্কিপ" else "Auto-Skip YouTube Shorts"
            "setting_skip_shorts_desc" -> if (isBn) "শর্টস স্ক্রলে অবাঞ্ছিত ট্যাগ বা কনটেন্ট পেলে অটো সোয়াইপ করে" else "Auto-swipes to next Short on match"
            "setting_toast_notify" -> if (isBn) "স্কিপ হলে টোস্ট নোটিফিকেশন" else "Show Toast on Auto-Skip"
            "setting_toast_desc" -> if (isBn) "ভিডিও স্কিপ করার সাথে সাথে স্ক্রিনে ছোট বার্তা দেখায়" else "Displays a brief popup when a video is skipped"
            "setting_security" -> if (isBn) "ডাটা সিকিউরিটি ও লোকাল স্টোরেজ" else "Data Security & Local Storage"
            "security_desc" -> if (isBn) "১০০% অফলাইন এবং লোকাল স্টোরেজ। আপনার কিওয়ার্ড বা হিস্ট্রি কোনো সার্ভারে পাঠানো হয় না।" else "100% On-device local storage. Your keywords and skip history never leave your device."
            "setting_battery" -> if (isBn) "ব্যাটারি ও পারফরম্যান্স অপ্টিমাইজেশন" else "Battery & Performance"
            "battery_desc" -> if (isBn) "অত্যন্ত লাইটওয়েট মেমোরি ক্যাশিং। সিপিইউ ও ব্যাটারির কোনো ক্ষতি করে না।" else "Zero background CPU drain with instant in-memory event caching."

            // Privacy Policy & Disclaimer
            "setting_privacy_policy" -> if (isBn) "প্রাইভেসি পলিসি ও ডিসক্লেইমার" else "Privacy Policy & Disclaimer"
            "privacy_policy_desc" -> if (isBn) "এক্সেসিবিলিটি ব্যবহার, ডাটা নিরাপত্তা, অ্যাডমব পলিসি ও দায়মুক্তি" else "Accessibility usage, data practices, AdMob disclosures, and legal disclaimer"
            "btn_open_privacy_policy" -> if (isBn) "প্রাইভেসি পলিসি পেজ খুলুন" else "Open Privacy Policy Page"
            "btn_open_disclaimer" -> if (isBn) "ডিসক্লেইমার পেজ খুলুন" else "Open Disclaimer Page"
            "btn_open_contact" -> if (isBn) "যোগাযোগ পেজ খুলুন" else "Open Contact Page"
            "btn_view_policy_dialog" -> if (isBn) "অ্যাপের ভেতর পলিসি পড়ুন" else "Read In-App Policy"
            "disclaimer_title" -> if (isBn) "ডিসক্লেইমার (Disclaimer)" else "Disclaimer"
            "accessibility_disclosure_title" -> if (isBn) "এক্সেসিবিলিটি পারমিশন অনুমোদন" else "Accessibility Permission Disclosure"
            "accessibility_disclosure_message" -> if (isBn) "এই অ্যাপটি শুধুমাত্র স্ক্রিনের ইউটিউব ইন্টারফেসের দৃশ্যমান টেক্সট শনাক্ত করতে এবং আপনার নির্ধারিত ভিডিও স্কিপ সম্পাদন করতে অ্যান্ড্রয়েড এক্সেসিবিলিটি সার্ভিস ব্যবহার করে। এক্সেসিবিলিটি ডাটা শুধুমাত্র এই কাজের জন্যই ব্যবহৃত হয়।" else "This app uses Android AccessibilityService to detect visible YouTube interface text and perform user-defined video skipping. Accessibility data is used only for this functionality."
            "btn_continue_to_settings" -> if (isBn) "বুঝেছি ও সেটিংসে যান" else "Continue to Settings"
            "btn_close" -> if (isBn) "বন্ধ করুন" else "Close"

            // Guide
            "guide_title" -> if (isBn) "এক্সেসিবিলিটি সার্ভিস চালু করার নিয়ম" else "How to Enable TubeSkip"
            "guide_step_1" -> if (isBn) "১. 'এক্সেসিবিলিটি চালু করুন' বাটনে চাপ দিন।" else "1. Tap 'Enable Accessibility' below."
            "guide_step_2" -> if (isBn) "২. 'Downloaded apps' বা 'ইনস্টল করা অ্যাপ' অপশনে যান।" else "2. Go to 'Downloaded apps' or 'Installed Services'."
            "guide_step_3" -> if (isBn) "৩. 'TubeSkip Auto-Skip Service' খুঁজে বের করে On / চালু করুন।" else "3. Find 'TubeSkip Auto-Skip Service' and toggle it ON."
            "guide_step_4" -> if (isBn) "৪. পারমিশন কনফার্ম করে পুনরায় এই অ্যাপে ফিরে আসুন।" else "4. Confirm system prompt and return to TubeSkip."
            "btn_got_it" -> if (isBn) "বুঝেছি" else "Got It"

            else -> key
        }
    }
}
