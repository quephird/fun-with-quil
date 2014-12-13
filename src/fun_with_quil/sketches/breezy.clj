(ns fun-with-quil.sketches.breezy
  (:use quil.core))

(def ^:constant screen-w 1920)
(def ^:constant screen-h 1080)

(defn r ^double [^long x ^long y]
  (loop [k 0 i 0.0 j 0.0]
    (let [si (sin i)
          cj (cos j)]
      (if (< k 15)
        (recur (inc k) (+ (- (* si si) (* cj cj)) (/ (- x 0.0) 1024.0)) (+ (* 2 si cj) (/ (- y 1024.0) 512.0)))
        (* 90.0 (abs j)))
      )
    )
  )

(defn g ^double [^long x ^long y]
  (loop [k 0 i 0.0 j 0.0]
    (let [si (sin i)
          cj (cos j)]
      (if (< k 15)
        (recur (inc k) (+ (- (* si si) (* cj cj)) (/ (- x 512.0) 1024.0)) (+ (* 2 si cj) (/ (- y 512.0) 512.0)))
        (* 90.0 (abs i)))
      )
    )
  )

(defn b ^double [^long x ^long y]
  (loop [k 0 i 0.0 j 0.0]
    (let [si (sin i)
          cj (cos j)]
      (if (< k 15)
        (recur (inc k) (+ (- (* si si) (* cj cj)) (/ (- x 1024.0) 1024)) (+ (* 2 si cj) (/ (- y 0.0) 512)))
        (* 50 (+ (sq i) (sq j))))
      )
    )
  )

(defn setup []
  (smooth)
  (no-loop)
  )

(defn draw []
  (doseq [x (range screen-w)
          y (range screen-h)]
    (let [c [(r x y) (g x y) (b x y)]]
      (apply stroke c)
      (point x y)
      )
     )
    (save "breezy.png")
  )

(sketch
  :title "breezy"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
