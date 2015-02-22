Feature: message board

  Scenario: empty board holds an invitation to post messages to the board
    Given an empty board
    Then the board holds the messages:
      | This board is empty so try and add some messages. |

  Scenario: post a message to an empty board
    Given an empty board
    When posting a message "M" to the board
    Then the board holds the messages:
      | M       |

  Scenario: post a message to a non empty board
    Given a board with messages:
      | A       |
    When posting a message "B" to the board
    Then the board holds the messages:
      | A       |
      | B       |

  Scenario: posting a message to a full board removes the oldest message
    Given a board with messages:
      | A       |
      | B       |
    And the board can hold "2" messages
    When posting a message "C" to the board
    Then the board holds the messages:
      | B       |
      | C       |

  Scenario Outline: post message attribute constraints
    Given usecase "post message"
    Then attribute "<name>" has constraint "<rule>"

  Examples:
    | name    | rule     |
    | message | required |

  Scenario Outline: post message header constraints
    Given usecase "post message"
    Then header "<name>" has constraint "<rule>"

  Examples:
    | name       | rule     |
    | access_key | required |
    | access_key | secret   |