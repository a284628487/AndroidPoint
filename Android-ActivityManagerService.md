### ActivityManagerService
startActivity -> startActivityAsUser

### ActivityStarter
-> execute -> startActivityMayWait -> startActivity -> startActivityUnchecked
-> setTaskFromReuseOrCreatedNewTask

### ActivityStack
-> moveToFront

### ActivityStackSuperivor
-> setFocusStackUnchecked

--------------------------------------------------------------------------

### ActivityManagerService
-> activityPaused(old)

### ActivityStack
-> activityPauseLocked(old) -> completePauseLocked(old)
-> resumeTopActivityUncheckedLocked -> resumeTopActivityInnerLocked

### ActivityStackSuperivor
-> startSpecificActivityLocked -> realStartActivityLocked

--------------------------------------------------------------------------
