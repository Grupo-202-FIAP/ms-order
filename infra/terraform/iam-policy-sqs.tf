resource "aws_iam_policy" "ms_order_sqs" {
  name = "ms-order-sqs-policy"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "sqs:ReceiveMessage",
        "sqs:DeleteMessage",
        "sqs:GetQueueAttributes"
      ]
      Resource = [
        data.aws_sqs_queue.order_queue.arn,
        data.aws_sqs_queue.order_callback_queue.arn
      ]
    }]
  })
}

resource "aws_iam_role_policy_attachment" "sqs_attach" {
  role       = aws_iam_role.ms_order_irsa.name
  policy_arn = aws_iam_policy.ms_order_sqs.arn
}
