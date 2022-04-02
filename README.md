# bopshare

## Table of Contents
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)

## Overview
### Description
An app for real people to create playlists and then be suggested other users (based on favorite genres) in dating app style. A user is recommended, their top three songs in the shared genre will start playing, and then user can swipe right or left on the recommendation. If user swipes right, they can view the rest of the recommended user's music and start adding songs from their playlists to their own.


### App Evaluation
- **Category:** Music Sharing
- **Mobile:** This app will be developed for mobile, but could potentially be viable on desktop or tablet later on.
- **Story:** Users are recommended songs from other user’s playlists and can swipe right or left the recommendation. If user swipes right, they can view more songs in the recommended user’s playlists.
- **Market:** Anyone who uses Spotify and likes getting recommended new songs
- **Habit:** This app can be used as frequently as a user desires, depending on how much new music user wants to hear.
- **Scope:** This app is a pairing to the existing Spotify app so users can have more recommendations. It can have as many users as Spotify itself.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User signs in through Spotify to access their account
* User picks their top 3 songs for each of their favorite genres
* A new user completes a short questionnaire to choose which playlists are publically available and which genres they want to display
* User can discover new users to follow by swiping right or left on their top 3 songs, which play automatically
* User can view recommended user's playlists once they’ve swiped right
* User can display profile information (about, following, followers, playlists)
* User can access the music and profiles of those they are following

**Optional Nice-to-have Stories**

* User can create new playlists or add to existing playlists in-app
* Settings section (accessiblity, notifications, general, etc.)
* User can choose whether to find music by shared/similar artists rather than by shared genres
* User can choose to have their genre and artist preferences auto-generated based on their playlists rather than choosing manually
* User can choose to get random recommendations (not based on any of their favorites) or popular recommendations (based on what most people are listening to)

### 2. Screen Archetypes

* Spotify login
* Discover
   * Allows user to discover other users with similar music taste by swiping right or left
* Profile
   * Allows user to view their playlists, bio, followers, and following. Also allows user to edit their top 3 songs for each of their favorite genres
* Followers
   * Allows user to view the profiles of those following them
* Following
   * Allows user to view the profiles of users they are following

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Discover
* Profile
* Logout

**Flow Navigation** (Screen to Screen)

* Discover
   * If swipe right, choice between continuing to discover or going to that user's profile
* Profile
   * Link to edit profile
   * Link to followers page
   * Link to following page
* Followers
   * List of users where each item is a link to that user's profile
* Following
   * List of users where each item is a link to that user's profile
