(ns procedural-lichens
  (:require [quil.core :as q :include-macros true]))

(def lichens (atom []))

(defn intersects? [[x1 y1 r1] [x2 y2 r2]]
  "Determines if the two circles passed in intersect"
  (let [dx (- x2 x1)
        dy (- y2 y1)
        distance-squared (+ (* dx dx) (* dy dy))
        sum-of-radii-squared (* (+ r1 r2) (+ r1 r2))]
    (<= distance-squared sum-of-radii-squared)))

(defn intersects-any? [test-circle existing-circles]
  "Determines if the test circle intersects with any of the existing circles"
  (some true? (map #(intersects? test-circle %) existing-circles)))

(defn random-circle [[min-x max-x min-y max-y min-r max-r] existing-circles]
  "Returns a random representation of a circle that does not intersect
   with the set of circles passed in. The parameters of the resultant circle
   are further restricted to the min and max values for x, y, and r."
  (let [x (+ min-x (q/random (- max-x min-x)))
        y (+ min-y (q/random (- max-y min-y)))
        r (+ min-r (q/random (- max-r min-r)))]
    (if (intersects-any? [x y r] existing-circles)
      (recur [min-x max-x min-y max-y min-r max-r] existing-circles)
      [x y r])))

(defn random-color [seed-c variance]
  "Returns a color that is a slight, random deviation from the seeded color"
  (let [dc (repeatedly 3 (fn [] (- (* variance 0.5) (q/random variance))))]
    (map + seed-c dc)))

(defn apothecia [max-r cup-c]
  "Renders the set of spore cups for the center of a particular lichen"
  (apply q/fill cup-c)
  (q/no-stroke)
  ; TODO:
  ;
  ; Need better distribution of cups
  (doseq [_ (range 20)]
    (let [x (q/random (- max-r) max-r)
          y (q/random (- max-r) max-r)
          w (q/random (* max-r 0.2) (* max-r 0.4))
          l (* w (q/random 1 1.2))
          φ (q/random 180)]
      (q/push-matrix)
      (q/rotate (q/radians φ))
      (q/ellipse x y w l)
      (q/pop-matrix))))


(defn- lichen [outer-r outer-lobes outer-c]
  "Renders a lichen, randomizing each of the lobes of each layer"
  (let [sw (/ (* q/PI outer-r) outer-lobes)
        multipliers [1 0.85 0.7]
        rs (map #(* % outer-r) multipliers)
        lobess (map #(* % outer-lobes) multipliers)
        cs (map (fn [m] (map (fn [c] (* m c)) outer-c)) multipliers)
        layers (map vector rs lobess cs)
        apothecia-r (* outer-r 0.6)
        apothecia-c (map #(* % 0.4) outer-c)]
    (q/stroke-weight sw)
    ; Render each of the three layers
    (doseq [[r lobes c] layers]
      (apply q/stroke c)
      (let [dθ (/ 360 lobes)]
        (doseq [_ (range lobes)]
          (let [left-dx (rand-int 20)
                right-dx (rand-int 20)
                dy (rand-int 20)]
            (q/bezier 0 0 0 0 0 (+ r dy) (- left-dx) (+ r dy))
            (q/bezier 0 0 0 0 0 (+ r dy) right-dx (+ r dy))
            (q/rotate (q/radians dθ))))))
    (apothecia apothecia-r apothecia-c)))

(defn all-lichens []
  (let [w (q/width)
        h (q/height)]
  (doseq [each_lichen (range 20)]
    (let [min-r (* 0.05 w)
          max-r (* 2.0 min-r)
          [x y r] (random-circle [0 w 0 h min-r max-r] @lichens)
          lobes (+ 20 (q/random 5))
          c (random-color [255 127 0] 50)]
;          c (random-color [151 252 152] 50)]
      (q/push-matrix)
      (q/translate x y)
      (lichen r lobes c)
      (q/pop-matrix)
      (swap! lichens conj [x y r])))))

(defn rock-surface []
  (let [w (q/width)
        h (q/height)
        min-sw (* 0.05 w)
        max-sw (* 2.0 min-sw)
        ]
    ; Base color
    (q/background (+ 20 (q/random 50)))
    (q/push-matrix)

    ; Base skew angle
    (q/rotate (q/radians (q/random 45)))
    (doseq [x (repeatedly 25 (fn [] (q/random w)))]
      ; Vary the thickness of each stroke
      (q/stroke-weight (q/random min-sw max-sw))

      ; Vary the shade of grey
      (q/stroke (q/random 50 100))

      ; Rotate each stroke a little bit from the baseline angle
      (q/push-matrix)
      (q/rotate (q/radians (q/random 10)))
      (q/line x (- h) x h)
      (q/pop-matrix))
    (q/pop-matrix)))

(defn setup []
  (q/smooth)
  (q/background 0)
  (q/no-loop))

(defn draw []
  ; TODO:
  ;
  ; Need to introduce clumping; randomly choose starting number 2 to 4 and clump around them
  (rock-surface)
  (all-lichens)
;  (save "procedural-lichens.png")
  )

(q/sketch
  :title "procedural lichens"
  :setup setup
  :draw draw
  :size [1400 800])
