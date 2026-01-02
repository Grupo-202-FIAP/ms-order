resource "aws_iam_policy" "ms_order_ssm" {
  name = "ms-order-ssm-policy"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "ssm:GetParameter",
        "ssm:GetParameters"
      ]
      Resource = "arn:aws:ssm:${data.aws_region.current.name}:<ACCOUNT_ID>:parameter/ms-order/*" //ToDo: ajustar
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ssm_attach" {
  role       = aws_iam_role.ms_order_irsa.name
  policy_arn = aws_iam_policy.ms_order_ssm.arn
}
