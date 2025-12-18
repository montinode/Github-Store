package zed.rainxch.githubstore.core.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscribed_developers")
data class SubscribedDeveloper(
    @PrimaryKey
    val developerLogin: String,
    
    val developerName: String?,
    val developerAvatarUrl: String,
    val developerBio: String?,
    
    val repositoryCount: Int = 0,
    val subscribedAt: Long, // timestamp
    val lastSyncedAt: Long // timestamp
)
