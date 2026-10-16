# API Contract Notes for the REST API Owner

## Profile

Existing planned endpoints:

- `GET /users/profile/{id}`
- `POST /users/profile`

Suggested JSON request:

```json
{
  "userId": "firebase-uid",
  "username": "MusicFan01",
  "profilePictureUrl": "https://...",
  "biography": "Music enthusiast",
  "favoriteGenres": ["Hip Hop", "R&B"],
  "favoriteArtists": ["SZA", "Kendrick Lamar"],
  "themePreference": "DARK"
}
```

The existing planning document explicitly specifies the first six user-profile fields. `themePreference` is added here to support the documented theme preference requirement.

## Daily challenge

The planning document says this feature needs an endpoint to fetch today's puzzle and an endpoint to submit an answer, but does not define endpoint paths. Proposed contract:

### GET /daily-challenge/today

```json
{
  "id": "challenge-2026-09-23",
  "date": "2026-09-23",
  "type": "ALBUM",
  "title": "Identify the Album",
  "clue": "...",
  "metadataFragment": "...",
  "artworkUrl": "https://...",
  "maxAttempts": 5
}
```

Do not send the answer to the client before the challenge is completed.

### POST /daily-challenge/answer

```json
{
  "challengeId": "challenge-2026-09-23",
  "userId": "firebase-uid",
  "answer": "Midnight Echoes"
}
```

Suggested response:

```json
{
  "correct": true,
  "completed": true,
  "attempts": 2,
  "streak": 7,
  "answer": "Midnight Echoes"
}
```

### GET /daily-challenge/leaderboard

Returns ranked users with current streak and all-time correct answers.

## Security

The Android client should send the existing Firebase ID token through the team's authenticated HTTP client. The API should verify that token before reading or writing user-specific profile/challenge data.
