(ns fun-with-quil.sketches.gingham
  (:use quil.core))

(def ^:constant screen-w 1920)
(def ^:constant screen-h 1080)

(defn make-gingham-fn [g d]
  "g controls the granularity of the squares
   d controls the distortion of the resultant image"
  (fn [x y]
    (let [s (/ 3.0 (+ y 99.0))]
      (+ (* (int (rem (* (+ (* (+ x d) s) (* s y)) g) 2)) 127)
         (* (int (rem (* (+ (* (- (* d 2) x) s) (* s y)) g) 2)) 127)))))

(defn setup []
  (smooth)
  (no-loop)
  )

(defn draw []
  (let [r (make-gingham-fn 0.25 1024)
        g (make-gingham-fn 3 1024)
        b (make-gingham-fn 15 1024)]
    (doseq [x (range screen-w)
            y (range screen-h)]
      (stroke (r x y) (g x y) (b x y))
      (point x y)))
    (save "gingham.png")
  )

(sketch
  :title "gingham"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
