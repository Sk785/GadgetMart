package com.gadgetmart.ui.dashboard

class SectionType {
    companion object {
        const val BANNER = "slider"
        const val POPULAR = "popular"
        const val RECENT = "recent"
        const val CUSTOM = "custom"

        fun isBanner(sectionType: String?): Boolean {
            return BANNER.equals(sectionType, ignoreCase = true)
        }

        fun isPopular(sectionType: String?): Boolean {
            return POPULAR.equals(sectionType, ignoreCase = true)
        }

        fun isRecent(sectionType: String?): Boolean {
            return RECENT.equals(sectionType, ignoreCase = true)
        }

        fun isCustom(sectionType: String?): Boolean {
            return CUSTOM.equals(sectionType, ignoreCase = true)
        }
    }
}