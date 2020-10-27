package ru.home.customvk.utils

import ru.home.customvk.models.local.Post
import ru.home.customvk.models.local.PostSource
import ru.home.customvk.models.network.Attachment
import ru.home.customvk.models.network.NewsfeedObject
import ru.home.customvk.models.network.PostNetworkModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object PostUtils {

    private const val PHOTO_ATTACHMENT_TYPE = "photo"

    fun List<Post>.filterByFavorites() = filter { it.isLiked }

    private fun Long.convertUnixTimeToHumanReadableDate(): String {
        val dateInMillisecond = Date(this * 1000)
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("GMT-4")
        return sdf.format(dateInMillisecond)
    }

    private fun PostNetworkModel.toPost(sourceName: String, sourceIconUrl: String) = Post(
        id = postId,
        source = PostSource(id = sourceId, name = sourceName, iconUrl = sourceIconUrl),
        publicationDate = date.convertUnixTimeToHumanReadableDate(),
        text = text,
        pictureUrl = attachments?.filter { it.type == PHOTO_ATTACHMENT_TYPE }?.takeFirstPhotoUrl() ?: "",
        likesCount = likes.count,
        isLiked = likes.isLiked == 1,
        commentsCount = comments.count,
        sharesCount = reposts.count,
        viewings = (views?.count ?: 0).toString()
    )


    /**
     * considered that all attachments have photo type.
     */
    private fun List<Attachment>.takeFirstPhotoUrl() =
        if (isNotEmpty()) {
            get(0).photo.sizes[0].url
        } else {
            ""
        }

    fun NewsfeedObject.toPosts(): List<Post> {
        val extractedPosts: MutableList<Post> = mutableListOf()
        posts.forEach { networkPost ->
            val sourceName: String
            val sourceIconUrl: String
            if (networkPost.sourceId > 0) {  // the condition means that some user is author of the post.
                val user = users.find { it.id == networkPost.sourceId }
                sourceName = "${user?.firstName} ${user?.lastName}"
                sourceIconUrl = user?.iconUrl ?: ""
            } else {  // either some group published the post.
                val group = groups.find { it.id == abs(networkPost.sourceId) }
                sourceName = group?.name ?: ""
                sourceIconUrl = group?.iconUrl ?: ""
            }
            extractedPosts.add(networkPost.toPost(sourceName, sourceIconUrl))
        }
        return extractedPosts
    }

}