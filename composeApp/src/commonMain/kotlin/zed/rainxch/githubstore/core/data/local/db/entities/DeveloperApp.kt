package zed.rainxch.githubstore.core.data.local.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "developer_apps",
    primaryKeys = ["developerLogin", "repoId"],
    foreignKeys = [
        ForeignKey(
            entity = SubscribedDeveloper::class,
            parentColumns = ["developerLogin"],
            childColumns = ["developerLogin"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DeveloperApp(
    val developerLogin: String,
    val repoId: Long,
    
    val repoName: String,
    val repoDescription: String?,
    val primaryLanguage: String?,
    
    val latestVersion: String?,
    val releaseUrl: String?,
    
    val isInstalled: Boolean = false,
    val installedPackageName: String? = null,
    
    val lastUpdatedAt: Long // timestamp
)