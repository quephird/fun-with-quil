(ns fun-with-quil.sketches.joukowsky
  (:use quil.core))

(def screen-w 1980)
(def screen-h 1080)

(defn hypot [a b]
  ; This check is in order to avoid an IllegalArgumentException due to float overflow
  (if (or (> a 10000) (> b 10000))
    10000
    (sqrt (+ (sq (float a)) (sq (float b))))))

(defn bl [x y]
  (let [r (- (/ x 512) 2)
        s (- (/ y 512) 2)
        q (+ (sq r) (sq s))
        n (hypot (+ r (/ (- 0.866 (/ r 2)) q)) (+ s (/ (+ (* r 0.866) (/ s 2)) q)))
        d (/ 0.5 (log n))
        e (if (or (< d 0) (> d 1)) 1 d)]
    (* d (+ (* (sin (* n 10)) 511) 512))))

(defn rd [x y]
  (let [r (- (/ x 512) 2)
        s (- (/ y 512) 2)
        q (+ (sq r) (sq s))]
    (* (bl x y) (sqrt (/ q 40)))))

(defn gr [x y]
  0)

(defn setup []
  (smooth)
  (no-loop))

(defn draw []
  (doseq [x (range screen-w)
          y (range screen-h)]
    (let [c [(rd x y) (gr x y) (bl x y)]]
      (apply stroke c)
      (point x y)))
    (save "joukowsky.png"))

(sketch
  :title "joukowsky"
  :setup setup
  :draw draw
  :size [screen-w screen-h])

