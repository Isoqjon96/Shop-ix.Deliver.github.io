package uz.mimsoft.shop_ixdeliver

import android.preference.PreferenceManager

object PManager{
    private val preferenceManager by lazy {
        return@lazy PreferenceManager.getDefaultSharedPreferences(App.getInstance())
    }

    fun getId() = preferenceManager.getString("deliverId", "") ?: ""
    fun setId(key: String) = preferenceManager.edit().putString("deliverId", key).apply()

    fun getName() = preferenceManager.getString("deliverName", "") ?: ""
    fun setName(name: String) = preferenceManager.edit().putString("deliverName", name).apply()

    fun getImage() = preferenceManager.getString("deliverImage", "") ?: ""
    fun setImage(image: String) = preferenceManager.edit().putString("deliverImage", image).apply()

    fun getPhone() = preferenceManager.getString("deliverPhone", "") ?: ""
    fun setPhone(phone: String) = preferenceManager.edit().putString("deliverPhone", phone).apply()

    fun getCompanyId() = preferenceManager.getLong("agentCompanyId", 0)
    fun setCompanyId(id: Long) = preferenceManager.edit().putLong("agentCompanyId", id).apply()

    fun getKey() = preferenceManager.getString("deliverKey", "") ?: ""
    fun setKey(key: String) = preferenceManager.edit().putString("deliverKey", key).apply()

    fun getLastVersionChecked() = preferenceManager.getLong("lastVersionChecked", 0L)
    fun setLastVersionChecked(time: Long) = preferenceManager.edit().putLong("lastVersionChecked", time).apply()

    fun getLanguage() = preferenceManager.getString("agentLanguage", "ru")?:"ru"
    fun setLanguage(language: String) = preferenceManager.edit().putString("agentLanguage", language).apply()

    fun clearData(){
        preferenceManager.edit().putString("agentKey", "").apply()
        preferenceManager.edit().putLong("lastVersionChecked", 0L).apply()
        preferenceManager.edit().putString("agentImage", "").apply()
        preferenceManager.edit().putString("agentName", "").apply()
        preferenceManager.edit().putString("agentPhone", "").apply()
        preferenceManager.edit().putLong("agentRegionId", -1L).apply()
        preferenceManager.edit().putLong("agentCompanyId", -1L).apply()
    }


}