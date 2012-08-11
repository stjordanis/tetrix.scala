import org.specs2._

class StageSpec extends Specification { def is = sequential ^
  "This is a specification to check Stage"                  ^
                                                            p^
  "The current piece should"                                ^
    """be initialized to the first element in the state.""" ! init1^
                                                            p^
  "Moving the current piece to the left should"             ^
    """change the blocks in the view,"""                    ! left1^
    """as long as it doesn't hit the wall"""                ! leftWall1^
    """or another block in the grid."""                     ! leftHit1^
                                                            p^
  "Moving the current piece to the right should"            ^
    """change the blocks in the view."""                    ! right1^
                                                            p^
  "Rotating the current piece should"                       ^
    """change the blocks in the view."""                    ! rotate1^
                                                            p^
  "Ticking the current piece should"                        ^
    """change the blocks in the view,"""                    ! tick1^
    """or spawn a new piece when it hits something."""      ! tick2^
    """It should also clear out full rows."""               ! tick3^
                                                            p^
  "Dropping the current piece should"                       ^
    """tick the piece until it hits something"""            ! drop1^
                                                            end
  
  import com.eed3si9n.tetrix._
  import Stage._
  val ttt = TKind :: TKind :: TKind :: Nil
  val s1 = newState(Block((0, 0), TKind) :: Nil, ttt)
  val s2 = newState(Block((3, 18), TKind) :: Nil, ttt)
  val s3 = newState(Seq(
      (0, 0), (1, 0), (2, 0), (3, 0), (7, 0), (8, 0), (9, 0))
    map { Block(_, TKind) }, ttt)
  val s4 = newState(Nil, OKind :: OKind :: Nil)
  def init1 =
    (s4.currentPiece.kind must_== OKind) and
    (s4.blocks map {_.pos} must contain(
      (4, 18), (5, 18), (4, 17), (5, 17)
    ).only.inOrder)
  def left1 =
    moveLeft(s1).blocks map {_.pos} must contain(
      (0, 0), (3, 18), (4, 18), (5, 18), (4, 19)
    ).only.inOrder
  def leftWall1 =
    Function.chain(moveLeft :: moveLeft :: moveLeft ::
        moveLeft :: moveLeft :: Nil)(s1).
      blocks map {_.pos} must contain(
      (0, 0), (0, 18), (1, 18), (2, 18), (1, 19)
    ).only.inOrder
  def leftHit1 =
    moveLeft(s2).blocks map {_.pos} must contain(
      (3, 18), (4, 18), (5, 18), (6, 18), (5, 19)
    ).only.inOrder
  def right1 =
    moveRight(s1).blocks map {_.pos} must contain(
      (0, 0), (5, 18), (6, 18), (7, 18), (6, 19)
    ).only.inOrder
  def rotate1 =
    rotateCW(s1).blocks map {_.pos} must contain(
      (0, 0), (5, 19), (5, 18), (5, 17), (6, 18)
    ).only.inOrder
  def tick1 =
    tick(s1).blocks map {_.pos} must contain(
      (0, 0), (4, 17), (5, 17), (6, 17), (5, 18)
    ).only.inOrder
  def tick2 =
    Function.chain(Nil padTo (19, tick))(s1).
    blocks map {_.pos} must contain(
      (0, 0), (4, 0), (5, 0), (6, 0), (5, 1),
      (4, 18), (5, 18), (6, 18), (5, 19)
    ).only.inOrder
  def tick3 =
    Function.chain(Nil padTo (19, tick))(s3).
    blocks map {_.pos} must contain(
      (5, 0), (4, 18), (5, 18), (6, 18), (5, 19)
    ).only.inOrder
  def drop1 =
    drop(s1).blocks map {_.pos} must contain(
      (0, 0), (4, 0), (5, 0), (6, 0), (5, 1),
      (4, 18), (5, 18), (6, 18), (5, 19)
    ).only.inOrder
}