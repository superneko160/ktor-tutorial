package com.example.model

// objectはシングルトンパターンを実装するための構文
// classと異なり、インスタンスが1つしか作成されないことを保証する
object TaskRepository {
    private val tasks =
        mutableListOf(
            Task("清掃", "家の掃除", Priority.Low),
            Task("ガーデニング", "草刈り、除草剤を撒く", Priority.Medium),
            Task("買い物", "夕食の材料購入", Priority.High),
            Task("プログラミング", "Kotlinの学習", Priority.Medium),
        )

    fun allTasks(): List<Task> = tasks.toList()

    fun tasksByPriority(priority: Priority) = tasks.filter { it.priority == priority }

    fun taskByName(name: String) = tasks.find { it.name.equals(name, true) }

    fun addTask(task: Task) {
        if (taskByName(task.name) != null) {
            throw IllegalStateException("同じ名前のタスクがすでに存在しています")
        }
        tasks.add(task)
    }
}
