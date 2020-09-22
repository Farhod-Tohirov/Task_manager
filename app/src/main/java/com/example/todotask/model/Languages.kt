package com.example.todotask.model

import com.example.todotask.data.Repository.LocalStorage

class Languages {


    private val localLang = LocalStorage.instance.lang

    /*Main Activity*/
    val tab1Text = if (localLang == ENGLISH_LANGUAGE) "Today" else "Bugun"
    val tab2Text = if (localLang == ENGLISH_LANGUAGE) "Coming" else "Yaqinlashayotgan"
    val tab3Text = if (localLang == ENGLISH_LANGUAGE) "Long-term" else "Uzoq muddatli"

    val addButton = if (localLang == ENGLISH_LANGUAGE) "Add note button" else "Qayd qo`shish tugmasi"
    val addButtonSubText =
        if (localLang == ENGLISH_LANGUAGE) "Tap this to create a new task." else "Yangi qaydnoma qo'shish uchun shu tugmani bosing."
    val menuButton = if (localLang == ENGLISH_LANGUAGE) "Menu Button" else "Menu tugmasi"
    val menuButtonSubText = if (localLang == ENGLISH_LANGUAGE) "Tap this to open menu" else "Menuni korish uchun shuni bosing"
    val infoSection = if (localLang == ENGLISH_LANGUAGE) "It is info section" else "Bu ma'lumotlar bo'limi"
    val infoSectionSubText =
        if (localLang == ENGLISH_LANGUAGE) "It shows statistics of tasks. Tap to see how many tasks you have, how many tasks you canceled and etc. :)" else "Shu yerni bossangiz nechta vazifangiz bajarilgan, nechtasini vaqti o'tib ketgan shu kabi savollarga javob olasiz \uD83D\uDE09"
    val doneTasksPercent = if (localLang == ENGLISH_LANGUAGE) "Done tasks' percent" else "Bajarilgan vazifalarning foiz miqdori"
    val doneTasksPercentSubText =
        if (localLang == ENGLISH_LANGUAGE) "This is the percentage of completed tasks" else "Bu bajarilgan vazifalarning umumiy vazifaga nisbatan foiz miqdori"
    val proportionOfTasks = if (localLang == ENGLISH_LANGUAGE) "Proportion of tasks" else "Vazifalar nisbati"
    val proportionOfTasksSubText =
        if (localLang == ENGLISH_LANGUAGE) "It shows proportion of done tasks and all tasks. First number is amount of completed tasks and second one is all tasks count. All tasks amount contains amount of canceled, completed and missed tasks count"
        else "Bu bajarilgan vazifalarning umumiy vazifaga nisbati. Birinchi son bajarilgan vazifalar soni, ikkinchi son esa barcha vazifalar. Barcha vazifalar miqdori deganda bekor qilingan, bajarilgan va vaqti o'tib ketgan vazifalar sonining yig'indisiga aytiladi"
    val taskCount = if (localLang == ENGLISH_LANGUAGE) "Amount of tasks" else "Vazifalar miqdori"
    val taskCountSubText =
        if (localLang == ENGLISH_LANGUAGE) "It show the amount of tasks count according to every page" else "Bu quyida berilgan bo'limlardagi vazifalarni miqdorini ko'rsatadi"
    val pageInfo = if (localLang == ENGLISH_LANGUAGE) "It is the pages of sorted tasks" else "Bu vazifalar bo'limi"
    val pageInfoSubText =
        if (localLang == ENGLISH_LANGUAGE) "Every task take place according to it's completion time. If task must be done today it take place to today page, if task should be done tomorrow or after tomorrow you can find it from coming page. Otherwise task take place from long-term page"
        else "Har bir vazifa o'zining bajarilish muddatiga ko'ra quyidagi bo'limlarga joylashadi. Agar vazifa bugun bajarilishi kerak bo'lsa \"bugun\" bo'limidan, ertaga yoki ertadan keyin bajarilishi kerak bo'lsa \"Yaqinlashayotgan\" bo'limidan topishingiz mumkin. Boshqa hollarda \"uzoq muddatli\" bo'limidan topasiz"
    val greetingText = if (localLang == ENGLISH_LANGUAGE) "Hello \uD83D\uDE0A" else "Salom \uD83D\uDE0A"
    val confirmationText =
        if (localLang == ENGLISH_LANGUAGE) "Do you want to read instruction for use?" else "Foydalanish qo'llanmasini o'qishni hohlaysizmi?"
    val yes = if (localLang == ENGLISH_LANGUAGE) "Yes\uD83D\uDE09" else "Albatta\\uD83D\\uDE09"
    val no = if (localLang == ENGLISH_LANGUAGE) "No\uD83D\uDE14" else "Keyinroq\uD83D\uDE14"
    val forToday = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "For Today" else "Bugun uchun"
    val forComing = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "For Coming" else "Yaqin kunlar uchun"
    val forLongTerm = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "For Long-term" else "Kelajak uchun"
    val youHave = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "you have" else "sizda"
    val tasksFor = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "tasks" else "ta vazifa mavjud"
    val today = if (localLang == ENGLISH_LANGUAGE) "Today" else "Bugun"
    val createANewTask = if (localLang == ENGLISH_LANGUAGE) "Create a new task \uD83D\uDE0A" else "Yangi vazifa yaratish \uD83D\uDE0A"
    val editTask = if (localLang == ENGLISH_LANGUAGE) "Edit task \uD83D\uDCDD" else "Vazifani taxrirlash\uD83D\uDCDD"
    val showTask = if (localLang == ENGLISH_LANGUAGE) "Show task" else "Vazifani ko'rish"
    val ok = if (localLang == ENGLISH_LANGUAGE) "Ok\uD83D\uDC4D" else "Ok\uD83D\uDC4D"
    val tasksProgression = if (localLang == ENGLISH_LANGUAGE) "Tasks Progression" else "Vazifalar bajarilishi"
    val allTasksCount = if (localLang == ENGLISH_LANGUAGE) "All Tasks count:" else "Barcha vazifalar miqdori:"
    val doneTasksCount = if (localLang == ENGLISH_LANGUAGE) "Done Tasks Count:" else "Bajarilgan vazifalar miqdori:"
    val canceledTasksCount = if (localLang == ENGLISH_LANGUAGE) "Canceled Tasks Count:" else "Bekor qilingan vazifalar miqdori:"
    val passedTasksCount = if (localLang == ENGLISH_LANGUAGE) "Passed Tasks Count:" else "Vaqti o'tib ketgan vazifalar miqdori:"
    val youHaveNoTask = if (localLang == ENGLISH_LANGUAGE) "You have no task yet" else "Sizda hali vazifa mavjud emas"
    val infoAuthor =
        if (localLang == ENGLISH_LANGUAGE) "Developer: Farhod Tohirov\nTelegram: @FarhodTohirov\nTask manager helps make tasks and notes to people. :)"
        else "Dasturchi: Farhod Tohirov\nTelegram: @FarhodTohirov\nTask manager insonlarga qaydnomalar yoki vazifalar yaratish uchun yordamlashadi :)"
    val tasksDone = if (localLang == ENGLISH_LANGUAGE) "Tasks done" else "Vazifa bajarilgan"

    val addTaskButton = if (localLang == ENGLISH_LANGUAGE) "Add Task" else "Vazifa qo'shish"
    val recycleBinButton = if (localLang == ENGLISH_LANGUAGE) "Recycle Bin" else "Vazifalar savatchasi"
    val allTasksButton = if (localLang == ENGLISH_LANGUAGE) "All tasks" else "Barcha vazifalar"
    val historyButton = if (localLang == ENGLISH_LANGUAGE) "History" else "Vazifalar tarixi"
    val settingsButton = if (localLang == ENGLISH_LANGUAGE) "Settings" else "Sozlamalar"
    val shareMeButton = if (localLang == ENGLISH_LANGUAGE) "Share me :)" else "Meni ulash :)"
    val instructionButton = if (localLang == ENGLISH_LANGUAGE) "Instructions for use" else "Foydalanish yoriqnomasi"
    val termsButton = if (localLang == ENGLISH_LANGUAGE) "Terms of use" else "Foydalanish shartlari"

    val create = if (localLang == ENGLISH_LANGUAGE) "Create" else "Saqlash"
    val close = if (localLang == ENGLISH_LANGUAGE) "Close" else "Chiqish"
    val title = if (localLang == ENGLISH_LANGUAGE) "Title" else "Sarlavha"
    val date = if (localLang == ENGLISH_LANGUAGE) "Date" else "Sana"
    val time = if (localLang == ENGLISH_LANGUAGE) "Time" else "Vaqt"
    val moreDetails = if (localLang == ENGLISH_LANGUAGE) "Write down details..." else "Batafsil malumotlarni yozing..."
    val crucial = if (localLang == ENGLISH_LANGUAGE) "Crucial" else "Juda muhim"
    val important = if (localLang == ENGLISH_LANGUAGE) "Important" else "Muhim"
    val free = if (localLang == ENGLISH_LANGUAGE) "Free" else "Bemalol"
    val urgency = if (localLang == ENGLISH_LANGUAGE) "Urgency" else "Muhimlik"
    val confirmationTextDialog = if (localLang == ENGLISH_LANGUAGE) "Please fill necessary fields" else "Kerakli qismlarni to'ldiring iltimos"
    val fillThis = if (localLang == ENGLISH_LANGUAGE) "Fill this" else "To'ldiring"
    val update = if (localLang == ENGLISH_LANGUAGE) "Update" else "Yangilash"
    val taskCanceled = if (localLang == ENGLISH_LANGUAGE) "Task is canceled \uD83D\uDEAB" else "Bekor qilingan\uD83D\uDEAB"
    val taskDeleted = if (localLang == ENGLISH_LANGUAGE) "Task is deleted \uD83D\uDDD1" else "Uchirilgan vazifa \uD83D\uDDD1"
    val taskDone = if (localLang == ENGLISH_LANGUAGE) "Task is done \uD83D\uDC4C" else "Vazifa bajarilgan\uD83D\uDC4C"
    val taskOutdated = if (localLang == ENGLISH_LANGUAGE) "Task is outdated \uD83D\uDDD1" else "Vaqti o'tgan \uD83D\uDDD1"
    val taskActive = if (localLang == ENGLISH_LANGUAGE) "Task is in active \uD83D\uDD14" else "Bajarilishi lozim \uD83D\uDD14"
    val restoration = if (localLang == ENGLISH_LANGUAGE) "Restoration" else "Tiklash"
    val clone = if (localLang == ENGLISH_LANGUAGE) "Clone" else "Nusxalash"

    /*Adapter Notes*/
    val delete = if (localLang == ENGLISH_LANGUAGE) "Delete" else "O'chirish"
    val done = if (localLang == ENGLISH_LANGUAGE) "Done" else "Bajarildi"
    val edit = if (localLang == ENGLISH_LANGUAGE) "Edit" else "Tahrirlash"
    val cancel = if (localLang == ENGLISH_LANGUAGE) "Cancel" else "Bekor qilish"

    /*Setting Activity*/
    fun showNotesAs(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Show notes as" else "Vazifalar quyidagicha ko'rinadi"
    fun grid(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Grid" else "Panjara"
    fun list(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "List" else "Ro'yhat"
    fun createNoteAs(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Create notes as" else "Vazifalarni quyidagicha yaratish"
    fun dialog(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Dialog" else "Kichik oyna"
    fun activity(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Activity" else "Katta oyna"
    fun language(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Language" else "Ilova tili"
    fun uzbek(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Uzbek" else "O'zbek"
    fun english(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "English" else "Ingliz tili"
    fun cancel(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Cancel" else "Bekor qilish"
    fun create(): String = if (LocalStorage.instance.lang == ENGLISH_LANGUAGE) "Create" else "Saqlash"

    /*All tasks Activity*/
    val passedTasks = if (localLang == ENGLISH_LANGUAGE) "Outdated Tasks" else "Vaqti o'tib ketgan vazifalar"
    val doneTasks = if (localLang == ENGLISH_LANGUAGE) "Done Tasks" else "Bajarilgan vazifalar"
    val canceledTasks = if (localLang == ENGLISH_LANGUAGE) "Canceled Tasks" else "Bekor qilingan vazifalar"
    val tasksHaveTime = if (localLang == ENGLISH_LANGUAGE) "Tasks Have Time" else "Bajarlishi lozim bo'lganlar"


    val createdSuccess = if (localLang == ENGLISH_LANGUAGE) "created successfully" else "muvaffaqiyatli qo'shildi"
    val didntCreated = if (localLang == ENGLISH_LANGUAGE) "Did not created \uD83D\uDE14 Check some information" else "Yaratilmadi \uD83D\uDE14 malumotlarni tekshiring"
    val smthWrong = if (localLang == ENGLISH_LANGUAGE) "Something wrong \uD83D\uDE22" else "Muammoga uchradi \uD83D\uDE22"
    val updateSuccess = if (localLang == ENGLISH_LANGUAGE) "Updated successfully \uD83D\uDC4D" else "Muvoffaqiyatli o'zgartirildi \uD83D\uDC4D"
    val taskRestarted = if (localLang == ENGLISH_LANGUAGE) "Task is restarted" else "Vazifa muvofaqiyatli qaytarildi"
    val taskCloned = if (localLang == ENGLISH_LANGUAGE) "Task is cloned" else "Vazifa nusxalandi"
    val taskdidntCloned = if (localLang == ENGLISH_LANGUAGE) "Task isn't cloned" else "Vazifa nusxalanmadi"
    val backedUp = if (localLang == ENGLISH_LANGUAGE) "Task backed up to list again \uD83D\uDC4D" else "Vazifa yana ro'yhatga qaytarildi \uD83D\uDC4D"
}