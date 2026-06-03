-- ============================================
-- PostgreSQL 建表 + Demo 数据
-- ============================================

-- 1. 建表
CREATE TABLE IF NOT EXISTS demo_items (
    id              BIGSERIAL       PRIMARY KEY,
    order_no        VARCHAR(50)     NOT NULL,
    customer_name   VARCHAR(100)    NOT NULL,
    product_name    VARCHAR(200)    NOT NULL,
    quantity        INTEGER         NOT NULL,
    unit_price      NUMERIC(12,2)   NOT NULL,
    total_amount    NUMERIC(12,2)   NOT NULL,
    order_date      DATE            NOT NULL,
    status          VARCHAR(20)     NOT NULL,
    remarks         TEXT,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL       PRIMARY KEY,
    username        VARCHAR(100)    NOT NULL UNIQUE,
    password        VARCHAR(255)    NOT NULL,
    full_name       VARCHAR(150),
    email           VARCHAR(200),
    enabled         BOOLEAN         NOT NULL DEFAULT true,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

INSERT INTO users (username, password, full_name, email, enabled)
VALUES ('admin', 'admin123', '系统管理员', 'admin@example.com', true)
ON CONFLICT (username) DO NOTHING;

-- 2. 插入 Demo 数据
INSERT INTO demo_items
    (order_no, customer_name, product_name, quantity, unit_price, total_amount, order_date, status, remarks)
VALUES
    ('ORD-2026-0001', '田中太郎',   'ノートPC',        2, 89800.00,  179600.00, '2026-05-01', '已完成', '急ぎ対応'),
    ('ORD-2026-0002', '佐藤花子',   'ワイヤレスマウス', 5,  2980.00,   14900.00, '2026-05-03', '已完成', NULL),
    ('ORD-2026-0003', '鈴木一郎',   'USB-C ハブ',      3,  4500.00,   13500.00, '2026-05-05', '处理中', '納期確認中'),
    ('ORD-2026-0004', '高橋美咲',   '27インチモニター', 1, 35000.00,   35000.00, '2026-05-08', '已完成', NULL),
    ('ORD-2026-0005', '渡辺健太',   'メカニカルキーボード', 2, 12800.00, 25600.00, '2026-05-10', '待处理', '在庫切れ'),
    ('ORD-2026-0006', '伊藤裕子',   'ウェブカメラ',     4,  6800.00,   27200.00, '2026-05-12', '处理中', NULL),
    ('ORD-2026-0007', '山本大輔',   'デスクライト',     2,  5500.00,   11000.00, '2026-05-14', '已完成', '領収書必要'),
    ('ORD-2026-0008', '中村さくら', 'ノートPCスタンド', 3,  3980.00,   11940.00, '2026-05-16', '待处理', NULL),
    ('ORD-2026-0009', '小林翔',     'Bluetoothスピーカー', 2, 7500.00, 15000.00, '2026-05-18', '处理中', '配送先確認'),
    ('ORD-2026-0010', '加藤あかり', 'モバイルバッテリー', 10, 2480.00, 24800.00, '2026-05-20', '已完成', '大量注文');

-- ============================================
-- 3. 语音录制表
-- ============================================
CREATE TABLE IF NOT EXISTS voice_recordings (
    id              BIGSERIAL       PRIMARY KEY,
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
    audio_data      BYTEA           NOT NULL,
    duration        INTEGER
);
COMMENT ON TABLE voice_recordings IS '语音录制记录';
COMMENT ON COLUMN voice_recordings.created_at IS '录制日期（系统时间）';
COMMENT ON COLUMN voice_recordings.audio_data IS '语音内容（二进制音频数据）';
COMMENT ON COLUMN voice_recordings.duration IS '语音时长（秒）';
