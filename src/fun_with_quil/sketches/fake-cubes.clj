(ns fun-with-quil.sketches.fake-cubes
  (:require [quil.core :as q :include-macros true]))

(defn fake-cube [x y s]
  (let [h (q/random 30 100)
        sa (q/random 127 255)]
    (q/push-matrix)
    (q/translate x y)
    (dotimes [i 3]
      (q/fill h sa (/ 255 (inc i)))
      (q/quad 0 0
              (* 0.866 s) (* -0.5 s)
              0 (- s)
              (* -0.866 s) (* -0.5 s))
      (q/rotate (/ q/TWO-PI 3)))
    (q/pop-matrix)))

(defn cell [level s x y]
  (let [coin-toss (rand-int 5)]
    (cond
      (or (> coin-toss 2) (= level 0))
        (fake-cube x y s)
      (and (> coin-toss 0) (> level 0))
        (dotimes [i 3]
          (let [sub-x (+ x (* 0.5 s (q/cos (q/radians (+ 30 (* i 120))))))
                sub-y (+ y (* 0.5 s (q/sin (q/radians (+ 30 (* i 120))))))
                sub-s (* 0.5 s)]
            (cell (dec level) sub-s sub-x sub-y)))
       :else nil)))

(defn setup []
  (q/smooth)
  (q/background 0)
  (q/no-stroke)
  (q/color-mode :hsb)
  (q/no-loop))

(defn draw []
  (let [w  (q/width)
        h  (q/height)
        s  100
        start-level 3
        dx (* s 1.732)
        dy (* s 1.5)]
    (doseq [c (range (+ 2 (int (/ w dx))))]
      (doseq [r (range (+ 2 (int (/ h dy))))]
        (let [x  (if (even? r) (* c dx) (* dx (+ c 0.5)))
              y  (* r dy)]
          (cell start-level s x y)))))
  (q/save "fake-cubes3.png"))

(q/defsketch fake-cubes
  :title "fake cubes"
  :setup setup
  :draw draw
  :size [2880 1800])
