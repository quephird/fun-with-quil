(ns fun-with-quil.sketches.tweetable
  (:use quil.core))

(def screen-w 1024)
(def screen-h screen-w)

(defn make-wave-fn []
  "[x1 y1] specifies the epicenter of the waves
   [x2 y2] specifies an anti-epicenter of sorts
   l controls the length between waves
   b controls the width of the crests"
  (let [[x1 y1] [(random 1024) (random 1024)]
        [x2 y2] [(random 1024) (random 1024)]
        l       (random 150)
        b       (random 2)]
    (fn [x y]
      (/ (+ (sqrt (+ (sq (- x1 x)) (sq (- y1 y)))) 1)
         (+ (sqrt (abs (sin (/ (sqrt (+ (sq (- x2 x)) (sq (- y2 y)))) l)))) b)
         3.0))))

(defn setup []
  (smooth)
  (no-loop))

(defn draw []
  (let [r-fn (make-wave-fn)
        g-fn (make-wave-fn)
        b-fn (make-wave-fn)]
    (doseq [x (range screen-w)
            y (range screen-h)]
      (apply stroke [(r-fn x y) (g-fn x y) (b-fn x y)])
      (point x y)))
  (save "wavey-thingy.png"))

(sketch
  :title "wavey-thingy"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
