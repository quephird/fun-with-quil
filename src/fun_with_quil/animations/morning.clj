(ns fun-with-quil.animations.morning
  (:require [quil.core :as q :include-macros true]))

(def θ (atom 0))

(defn setup []
  (q/smooth)
  (q/ellipse-mode :center)
  (q/stroke-weight 3))

(defn choose-color [c1 c2 n]
  (if (even? n) c1 c2))

(defn cell [w plus-or-minus c1 c2 c3]
  (let [fc (q/frame-count)
        circle-w (* w 0.75)
        half-circle-w (* circle-w 0.5)
        sw1 (* circle-w 0.65)
        hsw1 (* sw1 0.5)
        sw2 (* sw1 0.4)
        hsw2 (* sw2 0.5)
        θ  (* 2 fc)]
    ; rotate the cell
    (q/rotate (q/radians (plus-or-minus θ)))

    ; draw circle and 'crosshairs'
    (q/no-fill)
    (apply q/stroke c1)
    (q/ellipse 0 0 circle-w circle-w)
    (q/line (- half-circle-w) 0 half-circle-w 0)
    (q/line 0 (- half-circle-w) 0 half-circle-w)

    ; draw each of four squares with alternating colors
    (doseq [i (range 4)]
      (let [outside-stroke-c (choose-color c2 c3 i)
            inside-stroke-c (choose-color c3 c2 i)
            inside-fill-c (map #(/ % 2) inside-stroke-c)]
        (q/push-matrix)
        (q/translate half-circle-w 0)
        (q/rotate (q/radians 45))
        (q/no-fill)
        (apply q/stroke outside-stroke-c)
        (q/rect (- hsw1) (- hsw1) sw1 sw1)

        (apply q/stroke inside-stroke-c)
        (apply q/fill inside-fill-c)
        (q/rect (- hsw2) (- hsw2) sw2 sw2)
        (q/pop-matrix)
        (q/rotate (q/radians 90))))))

(defn draw []
  (let [bg [0 0 0]
        c1 [127 127 127]
        c2 [127 0 255]
        c3 [0 127 255]]
    (apply q/background bg)
    (q/translate 150 150)
    (doseq [r (range 4)]
      (q/push-matrix)
      (q/translate 0 (* r 150))
      (doseq [c (range 4)]
        (q/push-matrix)
        (q/translate (* c 150) 0)
        (cell 200 (if (even? (+ r c)) + -) c1 c2 c3)
        (q/pop-matrix))
      (q/pop-matrix))))

(q/sketch
  :title "violet and blue morning"
  :setup setup
  :draw draw
  :size [800 800])
