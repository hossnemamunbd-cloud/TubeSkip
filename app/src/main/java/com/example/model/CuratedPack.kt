package com.example.model

data class CuratedPack(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val iconName: String,
    val categoryName: String,
    val keywords: List<String>
) {
    companion object {
        fun getDefaultPacks(): List<CuratedPack> = listOf(
            CuratedPack(
                id = "clickbait_bengali",
                titleEn = "Bangla Clickbait & Sensationalism",
                titleBn = "বাংলা ক্লিকবেইট ও চটকদার খবর",
                descriptionEn = "Filters out sensational, misleading Bangla headlines and viral baits.",
                descriptionBn = "অতিরিক্ত অতিরঞ্জিত, ক্লিকবেইট ও ভুয়ো বিভ্রান্তিকর শিরোনাম ব্লক করে।",
                iconName = "bolt",
                categoryName = "Clickbait",
                keywords = listOf(
                    "দেখুন কি হলো",
                    "বিশ্বাস করতে পারবেন না",
                    "গোপন ফাঁস",
                    "ভিডিওটি দেখলে চমকে যাবেন",
                    "অবশেষে সত্যি প্রকাশ",
                    "হৃদয়বিদারক ঘটনা",
                    "কাউকে বলবেন না",
                    "ভাইরাল ভিডিও দেখুন",
                    "ভুলেও মিস করবেন না",
                    "সব শেষ হয়ে গেল",
                    "চরম সত্য ফাঁস",
                    "চোখ কপালে উঠবে"
                )
            ),
            CuratedPack(
                id = "clickbait_english",
                titleEn = "English Clickbait Baits",
                titleBn = "ইংরেজি ক্লিকবেইট ও ভাইরাল বেইট",
                descriptionEn = "Skips common generic YouTube clickbait triggers and spam headlines.",
                descriptionBn = "সাধারণ ইউটিউব ক্লিকবেইট ও ভাইরাল ট্র্যাপ শিরোনাম স্কিপ করে।",
                iconName = "flash_on",
                categoryName = "Clickbait",
                keywords = listOf(
                    "You Won't Believe",
                    "Shocking Truth",
                    "Gone Wrong",
                    "100% Real No Fake",
                    "Secret Revealed",
                    "Don't Watch This At 3AM",
                    "What Happened Next Will Shock You",
                    "OMG Must Watch",
                    "Police Called",
                    "Nearly Died"
                )
            ),
            CuratedPack(
                id = "spoilers",
                titleEn = "Movie & Series Spoilers",
                titleBn = "মুভি ও নাটক স্পয়লার",
                descriptionEn = "Prevents movie climax reveals, ending breakdowns, and plot spoilers.",
                descriptionBn = "সিনেমা, সিরিজ ও নাটকের ক্লাইম্যাক্স বা এন্ডিং ফাঁস হওয়া রোধ করে।",
                iconName = "movie",
                categoryName = "Spoilers",
                keywords = listOf(
                    "Ending Explained",
                    "Post Credit Scene",
                    "Death Scene",
                    "Full Movie Breakdown",
                    "Spoiler Warning",
                    "শেষের দৃশ্য",
                    "ক্লাইম্যাক্স ফাঁস",
                    "রহস্য ফাঁস",
                    "কে মারা গেল"
                )
            ),
            CuratedPack(
                id = "pranks_drama",
                titleEn = "Fake Pranks & Toxic Drama",
                titleBn = "ভুয়ো প্র্যাঙ্ক ও সোশ্যাল ড্রামা",
                descriptionEn = "Blocks scripted aggressive pranks, YouTuber feuds, and staged toxicity.",
                descriptionBn = "নাটকীয় ভুয়ো প্র্যাঙ্ক, রোস্টিং ঝগড়া ও অযথা সোশ্যাল ড্রামা দূর করে।",
                iconName = "theater",
                categoryName = "Pranks & Drama",
                keywords = listOf(
                    "Gold Digger Prank",
                    "Cheating Prank",
                    "Exposing YouTuber",
                    "Fight Caught On Camera",
                    "Roasting",
                    "রোস্ট ভিডিও",
                    "ঝগড়া মারামারি",
                    "প্র্যাঙ্ক কল",
                    "বেইজ্জত করলো"
                )
            ),
            CuratedPack(
                id = "brainrot_distractions",
                titleEn = "Brainrot & Distracting Shorts",
                titleBn = "ব্রেনরট ও সময় নষ্টকারী কনটেন্ট",
                descriptionEn = "Protects your focus from repetitive viral meme loops and addictive dopamine traps.",
                descriptionBn = "অতিরিক্ত স্ক্রোলিং ও সময় অপচয়কারী ভাইরাল মিম/শর্টস থেকে মনোযোগ রক্ষা করে।",
                iconName = "psychology",
                categoryName = "Distraction",
                keywords = listOf(
                    "Skibidi",
                    "Sigma Male",
                    "Gyatt",
                    "Mewing",
                    "Rage Bait",
                    "Satisfying Slime ASMR",
                    "Subway Surfers Gameplay Voiceover",
                    "Family Guy clips under game"
                )
            ),
            CuratedPack(
                id = "gossip_rumors",
                titleEn = "Celebrity Gossip & Rumors",
                titleBn = "সেলিব্রিটি পরচর্চা ও গুজব",
                descriptionEn = "Filters celebrity relationship rumors, breakups, and lifestyle paparazzi.",
                descriptionBn = "তারকাদের ব্যক্তিগত জীবন, বিয়ে-বিচ্ছেদ ও পরচর্চা বিষয়ক ভিডিও ফিল্টার করে।",
                iconName = "chat",
                categoryName = "Gossip",
                keywords = listOf(
                    "ডিভোর্স",
                    "গোপন বিয়ে",
                    "প্রেমের গুঞ্জন",
                    "বিয়ের ছবি ফাঁস",
                    "Affair",
                    "Breakup Leak",
                    "Secret Marriage",
                    "Court Hearing"
                )
            )
        )
    }
}
