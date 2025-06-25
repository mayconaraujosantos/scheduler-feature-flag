package com.banking.domain.dataccess

import com.banking.domain.entities.FeatureFlags

interface FeatureFlagDA {

    fun getFeatureFlag(): List<FeatureFlags>
}