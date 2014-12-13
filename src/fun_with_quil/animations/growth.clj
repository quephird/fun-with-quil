(ns fun-with-quil.animations.growth
  (:use quil.core))

(def circles (atom []))

(defn intersects? [[x1 y1 r1] [x2 y2 r2]]
  (let [dx (- x2 x1)
        dy (- y2 y1)
        distance-squared (+ (* dx dx) (* dy dy))
        sum-of-radii-squared (* (+ r1 r2) (+ r1 r2))]
    (<= distance-squared sum-of-radii-squared)))

(defn intersects-any? [circle existing-circles]
  (some true? (map #(intersects? circle %) existing-circles)))

(defn random-circle [existing-circles]
  ; Pick a circle from the list of existing ones
  ; Choose a random angle
  ; Choose a random r
  ; Compute x and y
  ; Check to see if this intersects any others
  ; If so, start over; if not return the circle
  (let [circle-idx (rand-int (count existing-circles))
        [x y r] (@circles circle-idx)
        θ (radians (rand-int 360))
        new-r (+ 5 (rand-int 45))
        new-x (+ x (* (+ r new-r) (cos θ)))
        new-y (+ y (* (+ r new-r) (sin θ)))]
    (if (intersects-any? [new-x new-y new-r] existing-circles)
      (random-circle existing-circles)
      [(int new-x) (int new-y) (int new-r)])))

(defn setup []
  (reset! circles [])
  (smooth)
  (background 0)
  (no-stroke)
  (ellipse-mode :center)
  (let [x (/ (width) 2)
        y (/ (height) 2)
        r (rand-int 100)
        d (* 2 r)]
    (fill 255 0 0)
    (ellipse x y d d)
    (swap! circles conj [x y r])))

; TODO: Need to determine how to either detect collisions with neighbors
;       or choose θ and/or the circle more wisely. Or maybe we need a different
;       data structure entirely; a tree instead of a flat vector.
(defn draw []
  (let [[new-x new-y new-r] (random-circle @circles)
        new-d (* 2 new-r)]
    (fill 255 0 0)
    (ellipse new-x new-y new-d new-d)
    (swap! circles conj [new-x new-y new-r])))

(sketch
  :title "growth"
  :setup setup
  :draw draw
  :size [1200 800])
