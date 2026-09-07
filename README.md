# ⚡ IMPULSE

## Pause the impulse. Keep the choice.

**IMPULSE** is a privacy-first Android app designed for the moment when you know you're about to do something you may regret—but can't seem to stop yourself.

It creates a pause between:

> **I NEED TO DO THIS NOW**

and:

> **I've done something I wish I hadn't.**

IMPULSE is particularly relevant to impulsivity, emotional activation, rumination and ADHD-related executive-function difficulties, but it is designed as a general behavioural self-management tool rather than an ADHD treatment app.

## The core idea

Sometimes the problem isn't knowing what you *should* do. You already know.

The problem is that, in the moment, the urge can become stronger and faster than the ability to pause.

IMPULSE does not try to eliminate emotions or tell users that their feelings are wrong. It creates **friction and time between an urge and an action**.

> **You are allowed to feel it. You are allowed to think it. You don't have to act on it immediately.**

## The loop

```text
TRIGGER
   ↓
EMOTIONAL ACTIVATION
   ↓
FIXATION / RUMINATION
   ↓
“I NEED TO DO SOMETHING”
   ↓
⚡ IMPULSE ⚡
   ↓
ACTION
   ↓
SHORT-TERM RELIEF / STIMULATION
   ↓
REGRET / CONSEQUENCES
   ↓
MORE EMOTION
```

IMPULSE intervenes here:

```text
EMOTIONAL ACTIVATION
        ↓
“I NEED TO DO SOMETHING”
        ↓
   ⚡ IMPULSE ⚡
        ↓
      PAUSE
        ↓
      CHOICE
```

## MVP

Version 0.1 deliberately focuses on the smallest useful experience:

1. **One-tap PAUSE ME button**
2. **30-minute default cooldown**
3. **Private local venting**
4. **Distraction/reset activities**
5. **Cooldown completion and reassessment**
6. **Local-only session history**
7. **Configurable cooldown duration**

The app should be usable when executive function is poor: minimal screens, large touch targets, clear language and no lengthy questionnaires.

## Intended experience

### 1. PAUSE ME

The home screen has one dominant action:

**🔴 PAUSE ME**

No unnecessary questions. The default cooldown begins immediately.

### 2. PRIVATE VENT

The user can write what they want to say without sending it anywhere.

> **Get it out.**
>
> Write exactly what you want to say. You don't need to make it reasonable. You don't need to make it polite. Nobody else has to see it.

Vent content remains on the device in the MVP.

### 3. RESET

During the cooldown, the app offers simple alternatives such as walking, music, showering, grounding, cleaning, distraction or contacting a trusted person.

### 4. REASSESS

When the cooldown ends:

**How do you feel now?**

- Still activated
- About the same
- A bit calmer
- Much calmer

The user chooses what to do next rather than being told what they should do.

## Planned Android app interruption

A major future feature is **temporary interruption of user-selected apps** such as social media, messaging, shopping or browser applications.

The intended experience is:

> **You asked us to stop you.**
>
> This app is currently paused.
>
> You don't have to solve this right now.

Implementation must use Android-supported mechanisms and respect platform, accessibility and Play Store requirements. Version 0.1 should not pretend to provide app blocking until the technical implementation has been properly tested.

## Privacy

IMPULSE may contain extremely personal emotional material.

Therefore:

- No account required.
- No cloud storage in the MVP.
- No advertising.
- No third-party analytics SDKs.
- No external processing of vent content.
- No AI analysis of private vents.
- Local-first data storage.
- User-controlled deletion.

Privacy is a product feature, not an afterthought.

## Psychology / evidence-informed design

IMPULSE is **evidence-informed, not a medical treatment**.

The design is informed by research concerning:

- Emotional dysregulation and adult ADHD.
- Impulsivity and preference for immediate rewards.
- Thought suppression and rebound effects.
- Behavioural interruption and creating time between an urge and an action.

The app must not claim that every user's behaviour is caused by ADHD, dopamine, emotional dysregulation or any other single mechanism.

See [`docs/psychology.md`](docs/psychology.md) for the evidence base and limitations.

## Safety

IMPULSE is not:

- A diagnostic tool.
- A replacement for therapy.
- A crisis service.
- A medical device unless a future version is specifically regulated and developed as such.

The app should provide appropriate crisis guidance when a user indicates immediate risk, while avoiding unnecessary alarm for ordinary emotional distress.

## Technology

The initial implementation should use:

- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **MVVM**
- **Room** for local structured data
- **DataStore** for preferences
- Android lifecycle-aware timer/state handling

Avoid unnecessary dependencies and over-engineering.

## Suggested project structure

```text
impulse/
├── app/
├── core/
│   ├── common/
│   ├── database/
│   ├── preferences/
│   └── security/
├── feature/
│   ├── home/
│   ├── pause/
│   ├── vent/
│   ├── cooldown/
│   ├── reset/
│   ├── complete/
│   └── settings/
├── domain/
├── data/
├── services/
└── docs/
```

## Roadmap

### 0.1 — Core intervention

- [ ] Android project foundation
- [ ] Home screen
- [ ] PAUSE ME
- [ ] Persistent cooldown timer
- [ ] Private vent
- [ ] Delete vent
- [ ] Reset/distraction menu
- [ ] Completion/reassessment
- [ ] Settings
- [ ] Basic tests

### 0.2 — App interruption

- [ ] User-selected app list
- [ ] Investigate Android-supported interruption mechanisms
- [ ] Usage/accessibility permission flow
- [ ] Interruption screen
- [ ] Cooldown-aware app state
- [ ] Robust lifecycle handling
- [ ] Accessibility and Play Store policy review

### 0.3 — Personalisation

- [ ] Custom reset menu
- [ ] Intensity rating
- [ ] Custom cooldowns
- [ ] Trusted contacts
- [ ] Home-screen widget

### 0.4 — Personal patterns

- [ ] Private session history
- [ ] Trigger/urge categories
- [ ] Intervention effectiveness
- [ ] User-controlled pattern insights

### Future

- Voice venting
- Wearable integrations
- Optional encrypted backup
- More sophisticated personalisation

## Product philosophy

IMPULSE should never shame the user.

Avoid:

> “You're being irrational.”

> “Stop doing that.”

Prefer:

> “You don't have to act on this right now.”

> “You can come back to this.”

> “Let's give you some time.”

> **“Protect tomorrow's you.”**

## Success metric

The primary success metric is not downloads, engagement or screen time.

It is:

> **Did IMPULSE create enough time for the user to make a different choice?**

If yes, the intervention has done its job.

## Contributing

IMPULSE is intended to become an open-source, privacy-first project. Contributions are welcome from Android developers, UX designers, accessibility specialists, behavioural scientists, psychologists and people with lived experience of impulsivity and emotional dysregulation.

## License

Apache 2.0 is currently proposed. Final licensing decision to be made before the first public release.

---

# ⚡ IMPULSE

### Pause the impulse. Keep the choice.

**Don't make your worst five minutes permanent.**
