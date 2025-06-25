package com.banking.resources

import com.banking.domain.dataccess.FeatureFlagDA
import com.banking.domain.entities.FeatureFlags
import com.banking.resources.gateways.FeatureFlagGateway
import org.springframework.stereotype.Component

@Component
class FeatureFlagDAImpl (
    private val featureFlagGateway: FeatureFlagGateway
): FeatureFlagDA {
    override fun getFeatureFlag(): List<FeatureFlags> {
       return featureFlagGateway.getFeatureFlagFromApi()
    }
}