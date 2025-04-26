-- 创建题目表
CREATE TABLE IF NOT EXISTS study_java_question (
    id VARCHAR(36) PRIMARY KEY,
    left_num INT NOT NULL,
    operator VARCHAR(1) NOT NULL,
    right_num INT NOT NULL,
    answer INT NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 创建错题表
CREATE TABLE IF NOT EXISTS study_java_wrong_question (
    id VARCHAR(36) PRIMARY KEY,
    question_id VARCHAR(36) NOT NULL,
    wrong_answer INT NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (question_id) REFERENCES study_java_question(id)
);

-- 创建进度表
CREATE TABLE IF NOT EXISTS study_java_progress (
    id VARCHAR(36) PRIMARY KEY,
    score INT NOT NULL DEFAULT 0,
    level INT NOT NULL DEFAULT 1,
    consecutive_correct INT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
); 