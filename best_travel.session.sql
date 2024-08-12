-- SELECT * FROM customer;
-- SELECT * FROM fly;
-- SELECT * FROM hotel;
-- SELECT * FROM reservation;
-- SELECT * FROM ticket;
SELECT * FROM tour;

-- SELECT * FROM tour 
--     JOIN reservation r ON tour.id = r.tour_id
--     JOIN hotel h ON h.id = r.hotel_id
--     JOIN customer c ON c.dni = r.customer_id;