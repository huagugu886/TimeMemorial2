import pathlib

p = pathlib.Path("app/src/main/java/com/huagugu/timememorial2/ui/screens/AddMemorialSheet.kt")
s = p.read_text(encoding="utf-8")

# Find and replace the viewModel.addMemorial block
import re

pattern = r'viewModel\.addMemorial\(\s*Memorial\(\s*title = title\.ifBlank \{ " 未命名纪念日" \},\s*date = selectedCal\.timeInMillis,\s*category = category\.name\s*\)\s*\)'
replacement = '''viewModel.addMemorial(
                        title = title.ifBlank { "未命名纪念日" },
                        date = selectedCal.timeInMillis,
                        category = category.name,
                        note = ""
                    )'''

if re.search(pattern, s):
    new_s = re.sub(pattern, replacement, s)
    p.write_text(new_s, encoding="utf-8")
    print("OK - replaced")
else:
    # Try without leading space in " 未命名纪念日"
    pattern2 = r'viewModel\.addMemorial\(\s*Memorial\(\s*title = title\.ifBlank \{[^\}]*\},\s*date = selectedCal\.timeInMillis,\s*category = category\.name\s*\)\s*\)'
    if re.search(pattern2, s):
        new_s = re.sub(pattern2, replacement, s)
        p.write_text(new_s, encoding="utf-8")
        print("OK - replaced (variant)")
    else:
        # Show context around viewModel.addMemorial
        idx = s.find("viewModel.addMemorial")
        if idx >= 0:
            print("NOT FOUND, context:")
            print(repr(s[idx:idx+250]))
        else:
            print("viewModel.addMemorial not found in file at all")
