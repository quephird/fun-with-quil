(ns fun-with-qui.sketches.looking-up
  (:use quil.core))

(def screen-w 1920)
(def screen-h 1080)

(defn rand-color [cs]
  (cs (rand-int (count cs))))

(defn leaf [l]
  (let [half-w (* 0.3 l)            half-l (* 0.5 l)
        bottom-x 0                  bottom-y 0
        top-x 0                     top-y (- bottom-y l)
        left-x (- bottom-x half-w)  left-y (- bottom-y half-l)
        right-x (+ bottom-x half-w) right-y (- bottom-y half-l)
        possible-leaf-colors        [[30 80 40]
                                     [40 100 40]
                                     [45 120 45]
                                     [50 140 50]
                                     [60 160 60]]
;        possible-leaf-colors        [[255 69 0]
;                                     [250 10 0]
;                                     [245 140 0]
;                                     [220 160 0]
;                                     [240 127 0]]
;        possible-leaf-colors        [[200 200 0]
;                                     [200 160 0]
;                                     [160 160 0]]
        leaf-color                  (rand-color possible-leaf-colors)
        vein-color                  (map #(/ % 2) leaf-color)]
    (no-stroke)
    (apply fill leaf-color)
    (begin-shape)
    (vertex bottom-x bottom-y)
    (bezier-vertex left-x left-y left-x left-y top-x top-y)
    (bezier-vertex right-x right-y right-x right-y bottom-x bottom-y)
    (end-shape)

    ; center vein
    (stroke-weight 2)
    (apply stroke vein-color)
    (line 0 0 top-x top-y)
    (no-fill)))

(defn leaf-group [l]
  ; draw two or three leaves
  (let [leaf-count (+ 2 (rand-int 2))
        start-θ (/ 180 leaf-count 2)
        delta-θ (* start-θ 4)]
    (push-matrix)
    (rotate (radians (rand-int start-θ)))
    (doseq [_ (range leaf-count)]
      (leaf l)
      (rotate (radians (rand-int delta-θ))))
    (pop-matrix)))

(defn- branch-iter [segment-w segment-l current-depth branch-depth]
  ; draw two branches, each at a slightly randomized angle

  (let [left-θ (- -5 (rand-int 25))
        right-θ (+ 5 (rand-int 25))
        left-l (+ segment-l (rand-int (* 0.4 segment-l)))
        right-l (+ segment-l (rand-int (* 0.4 segment-l)))]
    (doseq [[θ l] [[left-θ left-l] [right-θ right-l]]]
      (push-matrix)
      (rotate (radians θ))
      (stroke 40 20 0)
      (stroke-weight segment-w)
      (line 0 0 l 0)
      (translate l 0)

      ; draw leaves at end of branch segment only if we are in the last iteration
      (if (< current-depth branch-depth)
        (branch-iter (* segment-w 0.7) (* l 0.7) (inc current-depth) branch-depth)
        (leaf-group (* l 0.5)))
      (pop-matrix))))

(defn branch [start-segment-l start-segment-w z max-z]
  ; bigger z means greater distance from viewer.
  ; to try to simulate distance, branch segment widths and lengths are scaled down;
  ; blur values are scaled up.

  (let [scaled-segment-w (* (- max-z (dec z)) (/ start-segment-w max-z))
        scaled-segment-l (* (- max-z (dec z)) (/ start-segment-l max-z))
        blur-factor (* 2 (dec z))
        branch-depth 5]
    (branch-iter scaled-segment-w scaled-segment-l 1 branch-depth)

    (display-filter :blur blur-factor)))

(defn sky []
  ; for now, this just paints a background;
  ; TODO: perhaps a sun could be added later

  (background 127 127 255))

(defn tree [branch-θ start-segment-l start-branch-w]
  ; this draws branches in the background first.
  ; branches in the foreground are in focus; those farther away are blurred.

  (let [max-z 4]
    (doseq [z (range max-z 0 -1)]
      (push-matrix)
      (rotate (radians (+ branch-θ (- 20 (rand-int 40)))))
      (branch start-segment-l start-branch-w z max-z)
      (pop-matrix))))

(defn setup []
  (smooth)
  (no-loop))

(defn draw []
  (let [start-segment-l (* screen-w 0.25)
        start-branch-w (* start-segment-l 0.1)
        trunk-x (* screen-w 1.1)
        trunk-y (* screen-h 0.9)
        branch-θ 210]
    (sky)

    (translate trunk-x trunk-y)
    (tree branch-θ start-segment-l start-branch-w)
    (save "looking-up.png")))

(sketch
  :title "looking up"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
