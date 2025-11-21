-- 1. Quantos clientes temos na base?
SELECT
    COUNT(*) AS total_customers
FROM
    customers;

-- 2. Quantos quartos temos cadastrados?
SELECT
    COUNT(*) AS total_rooms
FROM
    rooms;

-- 3. Quantas reservas em aberto (agendadas ou em uso) o hotel possui no momento?
SELECT
    COUNT(*) AS open_reservations
FROM
    reservations
WHERE
    status IN ('SCHEDULED', 'IN_USE');

-- 4. Quantos quartos temos vagos no momento?
SELECT
    (SELECT COUNT(*) FROM rooms) - (SELECT COUNT(DISTINCT room_id) FROM reservations WHERE status = 'IN_USE') AS available_rooms;

-- 5. Quantos quartos temos ocupados no momento?
SELECT
    COUNT(DISTINCT room_id) AS occupied_rooms
FROM
    reservations
WHERE
    status = 'IN_USE';

-- 6. Quantas reservas futuras (agendadas) o hotel possui?
SELECT
    COUNT(*) AS future_reservations
FROM
    reservations
WHERE
    status = 'SCHEDULED';

-- 7. Qual o quarto mais caro do hotel?
SELECT
    room_number,
    price
FROM
    rooms
ORDER BY
    price DESC
    LIMIT 1;

-- 8. Qual o quarto com maior histórico de cancelamentos?
SELECT
    r.room_number,
    COUNT(*) AS cancellation_count
FROM
    reservations res
        JOIN
    rooms r ON res.room_id = r.id
WHERE
    res.status = 'CANCELED'
GROUP BY
    r.room_number
ORDER BY
    cancellation_count DESC
    LIMIT 1;

-- 9. Liste todos os quartos e a quantidade de clientes distintos que já ocuparam cada um.
SELECT
    r.room_number,
    COUNT(DISTINCT res.customer_id) AS distinct_customers_count
FROM
    rooms r
        LEFT JOIN
    reservations res ON r.id = res.room_id AND res.status IN ('IN_USE', 'FINISHED')
GROUP BY
    r.room_number
ORDER BY
    r.room_number;

-- 10. Quais são os 3 quartos que possuem um histórico maior de ocupações?
SELECT
    r.room_number,
    COUNT(res.id) AS occupations_count
FROM
    rooms r
        JOIN
    reservations res ON r.id = res.room_id
WHERE
    res.status IN ('IN_USE', 'FINISHED')
GROUP BY
    r.room_number
ORDER BY
    occupations_count DESC
    LIMIT 3;

-- 11. Quem são os 10 clientes com maior histórico de reservas (independente do status)?
SELECT
    c.name,
    COUNT(res.id) AS reservation_count
FROM
    customers c
        JOIN
    reservations res ON c.id = res.customer_id
GROUP BY
    c.id, c.name
ORDER BY
    reservation_count DESC
    LIMIT 10;

-- 12. Qual a receita total gerada com reservas 'FINISHED' e 'IN_USE'?
SELECT
    SUM(DATEDIFF('DAY', res.checkin, res.checkout) * r.price) AS total_revenue
FROM
    reservations res
        JOIN
    rooms r ON res.room_id = r.id
WHERE
    res.status IN ('FINISHED', 'IN_USE');
