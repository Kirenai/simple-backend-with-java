package une.revilla.backend.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TaskMessageEnum {
    REMOVED_BY_ADMIN_MODERATOR("The task has been successfully removed by the admin or moderator"),
    REMOVED_BY_USER("The task has been revomed seccessfully"),
    UPDATED_TASK("The user successfully update his task"),
    DOES_NOT_TASK_USER("The user does not have this task to assigned their warehouse"),
    TASK_NOT_FOUND("Task not found with Id: "),
    TASKS_NOT_FOUND("No tasks of this user with ID were found: "),
    USER_NOT_FOUND("User not found with Id: ");

    private String message;
}